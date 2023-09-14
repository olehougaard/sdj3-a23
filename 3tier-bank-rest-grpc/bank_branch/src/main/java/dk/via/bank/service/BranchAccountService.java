package dk.via.bank.service;

import dk.via.bank.data.*;
import dk.via.bank.model.*;

import java.rmi.RemoteException;
import java.util.Collection;

public class BranchAccountService  {
	private final int regNumber;
	private final AccountData accountData;

	public BranchAccountService(int regNumber, AccountData accountData) {
		this.regNumber = regNumber;
		this.accountData = accountData;
	}

	public Account createAccount(Customer customer, String currency) {
		return accountData.create(regNumber, customer, currency);
	}

	public Account getAccount(AccountNumber accountNumber) {
		return accountData.read(accountNumber);
	}
	
	public void cancelAccount(Account account) {
		accountData.delete(account);
	}

	public Collection<Account> getAccountsFor(Customer customer) {
		return accountData.readAccountsFor(customer);
	}
}
