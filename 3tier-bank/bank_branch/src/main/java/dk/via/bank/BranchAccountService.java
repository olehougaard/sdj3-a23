package dk.via.bank;

import dk.via.bank.data.*;
import dk.via.bank.model.*;

import java.rmi.RemoteException;
import java.util.Collection;

public class BranchAccountService  {
	private final int regNumber;
	private final AccountData accountData;

	public BranchAccountService(int regNumber, AccountData accountData) throws RemoteException {
		this.regNumber = regNumber;
		this.accountData = accountData;
	}

	public Account createAccount(Customer customer, String currency) throws RemoteException {
		return accountData.create(regNumber, customer, currency);
	}

	public Account getAccount(AccountNumber accountNumber) throws RemoteException {
		return accountData.read(accountNumber);
	}
	
	public void cancelAccount(Account account) throws RemoteException {
		accountData.delete(account);
	}

	public Collection<Account> getAccountsFor(Customer customer) throws RemoteException {
		return accountData.readAccountsFor(customer);
	}
}
