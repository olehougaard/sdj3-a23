package dk.via.bank.model.transaction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface TransactionVisitor<E extends Exception> {
	void visit(DepositTransaction transaction) throws E;
	void visit(WithdrawTransaction transaction) throws E;
	void visit(TransferTransaction transaction) throws E;
}
