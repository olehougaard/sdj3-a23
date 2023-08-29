package dk.via.cars;

import java.rmi.RemoteException;
import java.util.List;

public class RemoteCarBase implements CarBase {
	private final CarBaseImplementation implementation;

	public RemoteCarBase(CarDAO dao) {
		implementation = new CarBaseImplementation(dao);
	}
	
	@Override
	public Car registerCar(String licenseNumber, String model, int year, Money price) throws RemoteException {
		return implementation.registerCar(licenseNumber, model, year, price);
	}
	

	@Override
	public Car getCar(String licenseNumber) throws RemoteException {
		return implementation.getCar(licenseNumber);
	}

	@Override
	public List<Car> getAllCars() throws RemoteException {
		return implementation.getAllCars();
	}

	@Override
	public void removeCar(Car car) throws RemoteException {
		implementation.removeCar(car);
	}
}
