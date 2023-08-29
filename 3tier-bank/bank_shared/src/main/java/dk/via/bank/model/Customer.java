package dk.via.bank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cpr;
	private String name;
	private String address;
	private ArrayList<Account> accounts;

	public Customer(String cpr, String name, String address) {
		this.cpr = cpr;
		this.name = name;
		this.address = address;
		this.accounts = new ArrayList<>();
	}

	public String getCpr() {
		return cpr;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public void move(String address) {
		this.address = address;
	}
	
	public void addAccount(Account account) {
		accounts.add(account);
	}

	public Collection<Account> getAccounts() {
		return new ArrayList<Account>(accounts);
	}
}
