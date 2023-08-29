package dk.via.cars;

import java.io.Serializable;
import java.rmi.RemoteException;

public class CarDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String licenseNumber;
	private String model;
	private int year;
	private Money price;

	public CarDTO(String license_number, String model, int year, Money price) {
		this.licenseNumber = license_number;
		this.model = model;
		this.year = year;
		this.price = price;
	}
	
	public CarDTO(Car car) throws RemoteException {
		this(car.getLicenseNumber(), car.getModel(), car.getYear(), car.getPrice());
	}
	
	public String getLicenseNumber() {
		return licenseNumber;
	}

	public String getModel() {
		return model;
	}
	
	public int getYear() {
		return year;
	}
	
	public Money getPrice() {
		return price;
	}
}
