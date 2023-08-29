package dk.via.cars;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CarBase extends Remote {
	Car registerCar(String licenseNumber, String model, int year, Money price) throws RemoteException;
	Car getCar(String licenseNumber) throws RemoteException;
	List<Car> getAllCars() throws RemoteException;
	void removeCar(Car car) throws RemoteException;
}
