package dk.via.bank.model.transaction;

import java.rmi.RemoteException;

import dk.via.bank.model.Account;
import dk.via.bank.model.Money;

public class WithdrawTransaction extends AbstractTransaction {
	private static final long serialVersionUID = 1L;

	public WithdrawTransaction(Money amount, Account account) {
		this(amount, account, "Withdrew " + amount);
	}

	public WithdrawTransaction(Money amount, Account account, String text) {
		super(amount, account, text);
	}


	@Override
	public void accept(TransactionVisitor visitor) throws RemoteException {
		visitor.visit(this);
	}
}
