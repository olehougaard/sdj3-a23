package dk.via.bank.data;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Customer;
import dk.via.bank.model.Money;

public class AccountDataService extends UnicastRemoteObject implements AccountData {
	private static final long serialVersionUID = 1L;
	private final DatabaseHelper<Account> helper;
	
	public AccountDataService(String jdbcURL, String username, String password) throws RemoteException {
		helper = new DatabaseHelper<>(jdbcURL, username, password);
	}

	@Override
	public Account create(int regNumber, Customer customer, String currency)
			throws RemoteException {
		final List<Integer> keys = helper.executeUpdateWithGeneratedKeys("INSERT INTO Account(reg_number, customer, currency) VALUES (?, ?, ?)",
				regNumber, customer.getCpr(), currency);
		return read(new AccountNumber(regNumber, keys.get(0)));
	}

	private static Account createAccount(ResultSet rs) throws SQLException {
		AccountNumber accountNumber = new AccountNumber(rs.getInt("reg_number"), rs.getLong("account_number"));
		BigDecimal balance = rs.getBigDecimal("balance");
		String currency = rs.getString("currency");
		return new Account(accountNumber, new Money(balance, currency));
	}

	@Override
	public Account read(AccountNumber accountNumber) throws RemoteException {
		return helper.mapSingle(AccountDataService::createAccount, "SELECT * FROM Account WHERE reg_number = ? AND account_number = ? AND active",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber());
	}

	@Override
	public Collection<Account> readAccountsFor(Customer customer) throws RemoteException {
		return helper.map(AccountDataService::createAccount, "SELECT * FROM Account WHERE customer = ? AND active", customer.getCpr()) ;
	}

	@Override
	public void update(Account account) throws RemoteException {
		helper.executeUpdate("UPDATE ACCOUNT SET balance = ?, currency = ? WHERE reg_number = ? AND account_number = ? AND active",
				account.getBalance().getAmount(), account.getSettledCurrency(), account.getAccountNumber().getRegNumber(), account.getAccountNumber().getAccountNumber());
	}

	@Override
	public void delete(Account account) throws RemoteException {
		helper.executeUpdate("UPDATE ACCOUNT SET active = FALSE WHERE reg_number = ? AND account_number = ?",
				account.getAccountNumber().getRegNumber(), account.getAccountNumber().getAccountNumber());
	}
}
