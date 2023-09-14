package dk.via.bank.data;

import java.rmi.Remote;
import java.rmi.RemoteException;

import dk.via.bank.model.Customer;

public interface CustomerData extends Remote {
	public Customer create(String cpr, String name, String address);
	public Customer read(String cpr);
	public void update(Customer customer);
	public void delete(Customer customer);
}
