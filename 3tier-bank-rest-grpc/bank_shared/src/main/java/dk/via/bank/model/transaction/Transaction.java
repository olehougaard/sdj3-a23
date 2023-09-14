package dk.via.bank.model.transaction;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Money;

import java.io.Serializable;

public interface Transaction extends Serializable {
	Account getAccount();

	int getId();

	Money getAmount();

	String getText();
	<E extends Exception> void accept(TransactionVisitor<E> visitor) throws E;

	boolean includes(AccountNumber accountNumber);
}
