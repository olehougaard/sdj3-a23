package dk.via.bank.data;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataMapper<T> {
	T create(ResultSet rs) throws SQLException;
}
