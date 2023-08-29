package dk.via.cars;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
	public static void main(String[] args) throws RemoteException {
		RemoteCarBase carBase = new RemoteCarBase(DAOLocator.getDAO());
		Remote skeleton = UnicastRemoteObject.exportObject(carBase, 8080);
		Registry registry = LocateRegistry.getRegistry(1099);
		registry.rebind("CarBase", skeleton);
		System.out.println("Server running");
	}
}
