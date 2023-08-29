package dk.via.cars;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteCar extends UnicastRemoteObject implements Car {
	private static final long serialVersionUID = 1L;
	private String licenseNumber;
	private String model;
	private int year;
	private Money price;

	public RemoteCar(String licenseNumber, String model, int year, Money price) throws RemoteException {
		this.licenseNumber = licenseNumber;
		this.model = model;
		this.year = year;
		this.price = price;
	}
	
	public RemoteCar(CarDTO car) throws RemoteException {
		this(car.getLicenseNumber(), car.getModel(), car.getYear(), car.getPrice());
	}
	
	@Override
	public String getLicenseNumber() {
		return licenseNumber;
	}

	@Override
	public String getModel() {
		return model;
	}

	@Override
	public int getYear() {
		return year;
	}

	@Override
	public Money getPrice() {
		return price;
	}
	
	@Override
	public void setPrice(Money price) throws RemoteException {
		this.price = price;
		DAOLocator.getDAO().update(new CarDTO(this));
	}
}
