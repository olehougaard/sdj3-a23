package dk.via.bank.model.transaction;

import java.rmi.RemoteException;

import dk.via.bank.model.Account;
import dk.via.bank.model.Money;

public class DepositTransaction extends AbstractTransaction {
	private static final long serialVersionUID = 1L;

	public DepositTransaction(int id, Money amount, Account account) {
		this(id, amount, account, "Deposited " + amount);
	}
	
	public DepositTransaction(int id, Money amount, Account account, String text) {
		super(id, amount, account, text);
	}

	@Override
	public <E extends Exception> void accept(TransactionVisitor<E> visitor) throws E {
		visitor.visit(this);
	}

	// Marshalling
	public DepositTransaction() {}

}
