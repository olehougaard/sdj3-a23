package dk.via.bank.model.transaction;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Money;

public abstract class AbstractTransaction implements Transaction {
	private static final long serialVersionUID = 1L;
	private int id;
	private Money amount;
	private Account account;
	private String text;

	public AbstractTransaction(int id, Money amount, Account account, String text) {
		this.id = id;
		this.amount = amount;
		this.account = account;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Money getAmount() {
		return amount;
	}

	public Account getAccount() {
		return account;
	}

	public String getText() {
		return text;
	}

	public boolean includes(AccountNumber accountNumber) {
		return this.account.getAccountNumber().equals(accountNumber);
	}

	// Marshalling
	public AbstractTransaction() {}

	public void setAmount(Money amount) {
		this.amount = amount;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setText(String text) {
		this.text = text;
	}
}