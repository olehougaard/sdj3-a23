package dk.via.cars;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DAOLocator {

	public static CarDAO getDAO() throws RemoteException {
		Registry registry = LocateRegistry.getRegistry(1099);
		try {
			return (CarDAO) registry.lookup("carDao");
		} catch (NotBoundException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
