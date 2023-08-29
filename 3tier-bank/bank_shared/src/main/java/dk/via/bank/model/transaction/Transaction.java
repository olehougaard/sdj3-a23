package dk.via.bank.model.transaction;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Transaction extends Serializable {
	String getText();
	void accept(TransactionVisitor visitor) throws RemoteException;
}
