package dk.via.bank.data;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class AccountDataService {
	private final DatabaseHelper<Account> helper;
	
	public AccountDataService(@Value("${jdbc.url}") String jdbcURL,
							  @Value("${jdbc.username}") String username,
							  @Value("${jdbc.password}") String password) {
		this.helper = new DatabaseHelper<>(jdbcURL, username, password);
	}

	public Account create(int regNumber, String customerCpr, String currency) throws SQLException {
		final List<Integer> keys = helper.executeUpdateWithGeneratedKeys("INSERT INTO Account(reg_number, customer, currency) VALUES (?, ?, ?)",
				regNumber, customerCpr, currency);
		return read(new AccountNumber(regNumber, keys.get(0)));
	}

	private static Account createAccount(ResultSet rs) throws SQLException {
		AccountNumber accountNumber = new AccountNumber(rs.getInt("reg_number"), rs.getLong("account_number"));
		BigDecimal balance = rs.getBigDecimal("balance");
		String currency = rs.getString("currency");
		return new Account(accountNumber, new Money(balance, currency));
	}

	public Account read(AccountNumber accountNumber) throws SQLException {
		return helper.mapSingle(AccountDataService::createAccount, "SELECT * FROM Account WHERE reg_number = ? AND account_number = ? AND active",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber());
	}

	public Collection<Account> readAccountsFor(String customerCpr) throws SQLException {
		return helper.map(AccountDataService::createAccount, "SELECT * FROM Account WHERE customer = ? AND active", customerCpr) ;
	}

	public int update(Account account) throws SQLException {
		return helper.executeUpdate("UPDATE ACCOUNT SET balance = ?, currency = ? WHERE reg_number = ? AND account_number = ? AND active",
				account.getBalance().getAmount(), account.getSettledCurrency(), account.getAccountNumber().getRegNumber(), account.getAccountNumber().getAccountNumber());
	}

	public void delete(AccountNumber accountRegNumber) throws SQLException {
		helper.executeUpdate("UPDATE ACCOUNT SET active = FALSE WHERE reg_number = ? AND account_number = ?",
				accountRegNumber.getRegNumber(), accountRegNumber.getAccountNumber());
	}
}
