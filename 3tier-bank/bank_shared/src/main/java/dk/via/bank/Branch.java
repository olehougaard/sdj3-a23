package dk.via.bank;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Customer;
import dk.via.bank.model.Money;
import dk.via.bank.model.transaction.Transaction;

public interface Branch extends Remote {
	Customer createCustomer(String cpr, String name, String address) throws RemoteException;
	Customer getCustomer(String cpr) throws RemoteException;
	Account createAccount(Customer customer, String currency) throws RemoteException;
	Account getAccount(AccountNumber accountNumber) throws RemoteException;
	void cancelAccount(Account account) throws RemoteException;
	Collection<Account> getAccounts(Customer customer) throws RemoteException;
	void execute(Transaction t) throws RemoteException;
	Money exchange(Money amount, String targetCurrency) throws RemoteException;
	List<Transaction> getTransactionsFor(Account account) throws RemoteException;
}
