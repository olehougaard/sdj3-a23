package dk.via.bank.model.transaction;

import java.rmi.RemoteException;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Money;

public class TransferTransaction implements Transaction {
	private static final long serialVersionUID = 1L;
	private final WithdrawTransaction withdrawTransaction;
	private final DepositTransaction depositTransaction;
	private int id;

	public TransferTransaction(int id, Money amount, Account account, Account recipient) {
		this.id = id;
		this.withdrawTransaction = new WithdrawTransaction(id, amount, account, "Transferred " + amount + " to " + recipient);
		this.depositTransaction = new DepositTransaction(id, amount, recipient, "Transferred" + amount + "from " + recipient);
	}
	
	public TransferTransaction(int id, Money amount, Account account, Account recipient, String text) {
		this.id = id;
		this.withdrawTransaction = new WithdrawTransaction(id, amount, account, text);
		this.depositTransaction = new DepositTransaction(id, amount, recipient, text);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Money getAmount() {
		return withdrawTransaction.getAmount();
	}

	public Account getAccount() {
		return withdrawTransaction.getAccount();
	}
	
	public Account getRecipient() {
		return depositTransaction.getAccount();
	}
	
	public WithdrawTransaction getWithdrawTransaction() {
		return withdrawTransaction;
	}

	public DepositTransaction getDepositTransaction() {
		return depositTransaction;
	}

	@Override
	public <E extends Exception> void accept(TransactionVisitor<E> visitor) throws E {
		visitor.visit(this);
	}

	public boolean includes(AccountNumber accountNumber) {
		return withdrawTransaction.includes(accountNumber) || depositTransaction.includes(accountNumber);
	}

	@Override
	public String getText() {
		return withdrawTransaction.getText();
	}

	// Marshalling
	public TransferTransaction() {
		withdrawTransaction = new WithdrawTransaction();
		depositTransaction = new DepositTransaction();
	}
}
