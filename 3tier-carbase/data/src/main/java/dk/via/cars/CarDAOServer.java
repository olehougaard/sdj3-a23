package dk.via.cars;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

public class CarDAOServer extends UnicastRemoteObject implements CarDAO {
	private static final long serialVersionUID = 1;
	private final CarDAOImplementation implementation;

	public CarDAOServer(DatabaseHelper<CarDTO> helper) throws RemoteException {
		implementation = new CarDAOImplementation(helper);
	}

	@Override
	public CarDTO create(String licenseNo, String model, int year, Money price) throws RemoteException {
		return implementation.create(licenseNo, model, year, price);
	}

	@Override
	public CarDTO read(String licenseNumber) throws RemoteException {
		return implementation.read(licenseNumber);
	}

	@Override
	public Collection<CarDTO> readAll() throws RemoteException {
		return implementation.readAll();
	}

	@Override
	public void update(CarDTO car) throws RemoteException {
		implementation.update(car);
	}

	@Override
	public void delete(CarDTO car) throws RemoteException {
		implementation.delete(car);
	}
	
	public static void main(String[] args) throws Exception {
		DatabaseHelper<CarDTO> helper = new DatabaseHelper<>("jdbc:postgresql://localhost:5432/postgres?currentSchema=car_base", "postgres", "password");
		CarDAOServer carDAOServer = new CarDAOServer(helper);
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.rebind("carDao", carDAOServer);
	}
}
