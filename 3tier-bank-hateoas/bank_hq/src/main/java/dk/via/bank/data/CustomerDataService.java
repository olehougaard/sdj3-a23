package dk.via.bank.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import dk.via.bank.model.Account;
import dk.via.bank.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class CustomerDataService  {
	private static final long serialVersionUID = 1L;
	private final DatabaseHelper<Customer> helper;
	private final AccountDataService accountDAO;
	
	public CustomerDataService(@Value("${jdbc.url}") String jdbcURL,
							   @Value("${jdbc.username}") String username,
							   @Value("${jdbc.password}") String password,
							   AccountDataService accountDAO) {
		this.helper = new DatabaseHelper<>(jdbcURL, username, password);
		this.accountDAO = accountDAO;
	}

	private static Customer createCustomer(ResultSet rs) throws SQLException {
		String cpr = rs.getString("cpr");
		String name = rs.getString("name");
		String address = rs.getString("address");
		return new Customer(cpr, name, address);
	}

	public Customer create(String cpr, String name, String address) throws SQLException {
		helper.executeUpdate("INSERT INTO Customer VALUES (?, ?, ?)", cpr, name, address);
		return new Customer(cpr, name, address);
	}

	public Customer read(String cpr) throws SQLException {
		Customer customer =
				helper.mapSingle(CustomerDataService::createCustomer, "SELECT * FROM Customer WHERE cpr = ?;", cpr);
		Collection<Account> accounts = accountDAO.readAccountsFor(cpr);
		for(Account account: accounts) {
			customer.addAccount(account);
		}
		return customer;
	}

	public void update(Customer customer) throws SQLException {
		helper.executeUpdate("UPDATE Customer set name = ?, address = ? WHERE cpr = ?",
				customer.getName(),
				customer.getAddress(),
				customer.getCpr());
	}

	public void delete(String cpr) throws SQLException {
		helper.executeUpdate("DELETE FROM Customer WHERE cpr = ?", cpr);
	}
}
