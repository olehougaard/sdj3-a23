package dk.via.cars;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface CarDAO extends Remote {
	CarDTO create(String licenseNo, String model, int year, Money price) throws RemoteException;
	Collection<CarDTO> readAll() throws RemoteException;
	void update(CarDTO car) throws RemoteException;
	void delete(CarDTO car) throws RemoteException;
	CarDTO read(String licenseNumber) throws RemoteException;
}
