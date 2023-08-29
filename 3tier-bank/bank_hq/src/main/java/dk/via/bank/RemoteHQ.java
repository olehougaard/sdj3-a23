package dk.via.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import dk.via.bank.data.*;

public class RemoteHQ extends UnicastRemoteObject implements HeadQuarters {
	private static final long serialVersionUID = 1L;
	private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=bank";
	private static final String USERNAME = "postgres";
	private static final String PASSWORD = "password";
	
	private final ExchangeRateData exchangeData;
	private final AccountData accountData;
	private final CustomerData customerData;
	private final TransactionData transactionData;

	public RemoteHQ() throws RemoteException {
		exchangeData = new ExchangeRateDataService(JDBC_URL, USERNAME, PASSWORD);
		accountData = new AccountDataService(JDBC_URL, USERNAME, PASSWORD);
		transactionData = new TransactionDataService(accountData, JDBC_URL, USERNAME, PASSWORD);
		customerData = new CustomerDataService(JDBC_URL, USERNAME, PASSWORD, accountData);
	}

	@Override
	public ExchangeRateData getExchangeData() throws RemoteException {
		return exchangeData;
	}

	@Override
	public AccountData getAccountData() throws RemoteException {
		return accountData;
	}

	@Override
	public CustomerData getCustomerData() throws RemoteException {
		return customerData;
	}

	@Override
	public TransactionData getTransactionData() throws RemoteException {
		return transactionData;
	}
}
