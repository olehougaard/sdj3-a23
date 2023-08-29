package dk.via.bank.data;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HeadQuarters extends Remote {
	ExchangeRateData getExchangeData() throws RemoteException;
	AccountData getAccountData() throws RemoteException;
	CustomerData getCustomerData() throws RemoteException;
	TransactionData getTransactionData() throws RemoteException;
}
