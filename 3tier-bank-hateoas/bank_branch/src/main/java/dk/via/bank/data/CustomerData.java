package dk.via.bank.data;

import java.rmi.Remote;
import java.rmi.RemoteException;

import dk.via.bank.model.Customer;

public interface CustomerData extends Remote {
	public Customer create(String cpr, String name, String address) throws RemoteException;
	public Customer read(String cpr) throws RemoteException;
	public void update(Customer customer) throws RemoteException;
	public void delete(Customer customer) throws RemoteException;
}
