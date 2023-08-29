package dk.via.cars;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.Collection;

public class CarDAOImplementation {
	private DatabaseHelper<CarDTO> helper;

	public CarDAOImplementation(DatabaseHelper<CarDTO> helper) {
		this.helper = helper;
	}
	
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=car_base", "postgres", "password");
	}

	public CarDTO create(String licenseNo, String model, int year, Money price) throws RemoteException {
		helper.executeUpdate("INSERT INTO car VALUES (?, ?, ?, ?, ?)", licenseNo, model, year, price.getAmount(), price.getCurrency());
		return new CarDTO(licenseNo, model, year, price);
	}
	
	private CarDTO createCar(ResultSet rs) throws SQLException {
		String licenseNo = rs.getString("license_number");
		String model = rs.getString("model");
		int year = rs.getInt("year");
		BigDecimal priceAmount = rs.getBigDecimal("price_amount");
		String priceCurrency = rs.getString("price_currency");
		Money price = new Money(priceAmount, priceCurrency);
		return new CarDTO(licenseNo, model, year, price);
	}

	public CarDTO read(String licenseNumber) throws RemoteException {
		return helper.mapSingle(this::createCar, "SELECT * FROM car where license_number = ?", licenseNumber);
	}

	public Collection<CarDTO> readAll() throws RemoteException {
		return helper.map(this::createCar, "SELECT * FROM car");
	}

	public void update(CarDTO car) throws RemoteException {
		helper.executeUpdate("UPDATE car SET model=?, year=?, price_amount=?, price_currency=? WHERE license_number = ?", 
				car.getModel(), car.getYear(), car.getPrice().getAmount(), car.getPrice().getCurrency(), car.getLicenseNumber());
	}

	public void delete(CarDTO car) throws RemoteException {
		helper.executeUpdate("DELETE FROM car WHERE license_number = ?", car.getLicenseNumber());
	}
}
