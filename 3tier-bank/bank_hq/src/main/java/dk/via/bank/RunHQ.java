package dk.via.bank;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunHQ {
	public static void main(String[] args) throws AccessException, RemoteException {
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.rebind("HQ", new RemoteHQ());
		System.out.println("HQ started");
	}
}
