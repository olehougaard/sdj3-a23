package dk.via.bank.model.transaction;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TransactionVisitor {
	void visit(DepositTransaction transaction) throws RemoteException;
	void visit(WithdrawTransaction transaction) throws RemoteException;
	void visit(TransferTransaction transaction) throws RemoteException;
}
