package dk.via.bank.data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import dk.via.bank.model.Account;
import dk.via.bank.model.Customer;

public class CustomerDataService extends UnicastRemoteObject implements CustomerData {
	private static final long serialVersionUID = 1L;
	private final DatabaseHelper<Customer> helper;
	private final AccountData accountDAO;
	
	public CustomerDataService(String jdbcURL, String username, String password, AccountData accountDAO) throws RemoteException {
		this.accountDAO = accountDAO;
		this.helper = new DatabaseHelper<>(jdbcURL, username, password);
	}

	private static Customer createCustomer(ResultSet rs) throws SQLException {
		String cpr = rs.getString("cpr");
		String name = rs.getString("name");
		String address = rs.getString("address");
		return new Customer(cpr, name, address);
	}

	@Override
	public Customer create(String cpr, String name, String address) throws RemoteException {
		helper.executeUpdate("INSERT INTO Customer VALUES (?, ?, ?)", cpr, name, address);
		return new Customer(cpr, name, address);
	}

	@Override
	public Customer read(String cpr) throws RemoteException {
		Customer customer =
				helper.mapSingle(CustomerDataService::createCustomer, "SELECT * FROM Customer WHERE cpr = ?;", cpr);
		Collection<Account> accounts = accountDAO.readAccountsFor(customer);
		for(Account account: accounts) {
			customer.addAccount(account);
		}
		return customer;
	}

	@Override
	public void update(Customer customer) throws RemoteException {
		helper.executeUpdate("UPDATE Customer set name = ?, address = ? WHERE cpr = ?",
				customer.getName(),
				customer.getAddress(),
				customer.getCpr());
	}

	@Override
	public void delete(Customer customer) throws RemoteException {
		helper.executeUpdate("DELETE FROM Customer WHERE cpr = ?", customer.getCpr());
	}
}
