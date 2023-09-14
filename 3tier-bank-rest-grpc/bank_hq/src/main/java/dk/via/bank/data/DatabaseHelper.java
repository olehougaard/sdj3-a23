package dk.via.bank.data;

import org.postgresql.Driver;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper<T> {
	private final String jdbcURL;
	private final String username;
	private final String password;
	
	public DatabaseHelper(String jdbcURL, String username, String password)  {
		this.jdbcURL = jdbcURL;
		this.username = username;
		this.password = password;
		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("No JDBC driver");
		}
	}
	
	public DatabaseHelper(String jdbcURL) {
		this(jdbcURL, null, null);
	}
	
	public Connection getConnection() throws SQLException {
		if (username == null) {
			return DriverManager.getConnection(jdbcURL);
		} else {
			return DriverManager.getConnection(jdbcURL, username, password);
		}
	}

	private static PreparedStatement prepare(Connection connection, String sql, Object... parameters) throws SQLException {
		PreparedStatement stat = connection.prepareStatement(sql);
		for(int i = 0; i < parameters.length; i++) {
			stat.setObject(i + 1, parameters[i]);
		}
		return stat;
	}
	
	private static PreparedStatement prepareWithKeys(Connection connection, String sql, Object... parameters) throws SQLException {
		PreparedStatement stat = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		for(int i = 0; i < parameters.length; i++) {
			stat.setObject(i + 1, parameters[i]);
		}
		return stat;
	}
	
	public ResultSet executeQuery(Connection connection, String sql, Object... parameters) throws SQLException {
		PreparedStatement stat = prepare(connection, sql, parameters);
		return stat.executeQuery();
	}
	
	public int executeUpdate(String sql, Object... parameters) throws SQLException {
		try (Connection connection = getConnection()) {
			PreparedStatement stat = prepare(connection, sql, parameters);
			return stat.executeUpdate();
		}
	}
	
	public List<Integer> executeUpdateWithGeneratedKeys(String sql, Object... parameters) throws SQLException {
		try (Connection connection = getConnection()) {
			PreparedStatement stat = prepareWithKeys(connection, sql, parameters);
			stat.executeUpdate();
			LinkedList<Integer> keys = new LinkedList<>();
			ResultSet rs = stat.getGeneratedKeys();
			while(rs.next()) {
				keys.add(rs.getInt(1));
			}
			return keys;
		}
	}
	
	public T mapSingle(DataMapper<T> mapper, String sql, Object... parameters) throws SQLException {
		try (Connection connection = getConnection()) {
			ResultSet rs = executeQuery(connection, sql, parameters);
			if(rs.next()) {
				return mapper.create(rs);
			} else {
				return null;
			}
		}
	}

	public List<T> map(DataMapper<T> mapper, String sql, Object... parameters) throws SQLException {
		try (Connection connection = getConnection()) {
			ResultSet rs = executeQuery(connection, sql, parameters);
			LinkedList<T> allCars = new LinkedList<>();
			while(rs.next()) {
				allCars.add(mapper.create(rs));
			}
			return allCars;
		}
	}
}
