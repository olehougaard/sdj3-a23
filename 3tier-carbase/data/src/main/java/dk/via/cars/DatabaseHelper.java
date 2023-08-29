package dk.via.cars;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.postgresql.Driver;

public class DatabaseHelper<T> {
	private String jdbcURL;
	private String username;
	private String password;
	
	public DatabaseHelper(String jdbcURL, String username, String password) throws RemoteException {
		this.jdbcURL = jdbcURL;
		this.username = username;
		this.password = password;
		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException e) {
			throw new RemoteException("No JDBC driver", e);
		}
	}
	
	public DatabaseHelper(String jdbcURL) throws RemoteException {
		this(jdbcURL, null, null);
	}
	
	protected Connection getConnection() throws SQLException {
		if (username == null) {
			return DriverManager.getConnection(jdbcURL);
		} else {
			return DriverManager.getConnection(jdbcURL, username, password);
		}
	}

	private PreparedStatement prepare(Connection connection, String sql, Object[] parameters) throws SQLException {
		PreparedStatement stat = connection.prepareStatement(sql);
		for(int i = 0; i < parameters.length; i++) {
			stat.setObject(i + 1, parameters[i]);
		}
		return stat;
	}

	public int executeUpdate(String sql, Object... parameters) throws RemoteException {
		try (Connection connection = getConnection()) {
			PreparedStatement stat = prepare(connection, sql, parameters);
			return stat.executeUpdate();
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}
	
	public T mapSingle(DataMapper<T> mapper, String sql, Object... parameters) throws RemoteException {
		try (Connection connection = getConnection()) {
			PreparedStatement stat = prepare(connection, sql, parameters);
			ResultSet rs = stat.executeQuery();
			if(rs.next()) {
				return mapper.create(rs);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public List<T> map(DataMapper<T> mapper, String sql, Object... parameters) throws RemoteException {
		try (Connection connection = getConnection()) {
			PreparedStatement stat = prepare(connection, sql, parameters);
			ResultSet rs = stat.executeQuery();
			LinkedList<T> allCars = new LinkedList<>();
			while(rs.next()) {
				allCars.add(mapper.create(rs));
			}
			return allCars;
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}
}
