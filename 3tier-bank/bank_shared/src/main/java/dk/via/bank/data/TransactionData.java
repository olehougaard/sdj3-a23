package dk.via.bank.data;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import dk.via.bank.model.Account;
import dk.via.bank.model.transaction.Transaction;

public interface TransactionData extends Remote {
	Transaction read(int transactionId) throws RemoteException;
	List<Transaction> readAllFor(Account account) throws RemoteException;
	void create(Transaction transaction) throws RemoteException;
	void deleteFor(Account account) throws RemoteException;
}
