package dk.via.bank.service;

import dk.via.bank.data.*;
import dk.via.bank.model.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.util.Collection;

@Component
public class BranchAccountService  {
	private final int regNumber;
	private final AccountData accountData;

	public BranchAccountService(AccountData accountData, @Value("${branch.regNumber}") Integer regNumber) {
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
