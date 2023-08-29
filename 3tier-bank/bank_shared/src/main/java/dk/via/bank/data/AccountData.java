package dk.via.bank.data;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Customer;

public interface AccountData extends Remote {
	public Account create(int regNumber, Customer customer, String currency) throws RemoteException;
	public Account read(AccountNumber accountNumber) throws RemoteException;
	public Collection<Account> readAccountsFor(Customer customer) throws RemoteException;
	public void update(Account account) throws RemoteException;
	public void delete(Account account) throws RemoteException;
}
