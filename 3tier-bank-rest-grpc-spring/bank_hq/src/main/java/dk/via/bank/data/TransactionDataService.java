package dk.via.bank.data;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Money;
import dk.via.bank.model.transaction.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Scope("singleton")
public class TransactionDataService {
	private static final String DEPOSIT = "Deposit";
	private static final String TRANSFER = "Transfer";
	private static final String WITHDRAWAL = "Withdrawal";
	
	private final DatabaseHelper<Transaction> helper;
	private final AccountDataService accounts;
	
	public TransactionDataService(AccountDataService accounts, DatabaseHelper<Transaction> helper) {
		this.accounts = accounts;
		this.helper = helper;
	}

	private Account readAccount(ResultSet rs, String regNumberAttr, String acctNumberAttr) throws SQLException {
		return accounts.read(new AccountNumber(rs.getInt(regNumberAttr), rs.getLong(acctNumberAttr)));
	}

	private Transaction createTransaction(ResultSet rs) throws SQLException {
		int id = rs.getInt("transaction_id");
		Money amount = new Money(rs.getBigDecimal("amount"), rs.getString("currency"));
		String text = rs.getString("transaction_text");
		Account primary = readAccount(rs, "primary_reg_number", "primary_account_number");
		switch(rs.getString("transaction_type")) {
		case DEPOSIT:
			return new DepositTransaction(id, amount, primary, text);
		case WITHDRAWAL:
			return new WithdrawTransaction(id, amount, primary, text);
		case TRANSFER:
			Account secondaryAccount = readAccount(rs, "secondary_reg_number", "secondary_account_number");
			return new TransferTransaction(id, amount, primary, secondaryAccount, text);
		default:
			return null;
		}
	}

	private class TransactionCreator implements TransactionVisitor<SQLException> {
		public void visit(DepositTransaction transaction) throws SQLException {
			Money amount = transaction.getAmount();
			AccountNumber primaryAccount = transaction.getAccount().getAccountNumber();
			List<Integer> keys = helper.executeUpdateWithGeneratedKeys(
					"INSERT INTO Transaction(amount, currency, transaction_type, transaction_text, primary_reg_number, primary_account_number) VALUES (?, ?, ?, ?, ?, ?)",
					amount.getAmount(),
					amount.getCurrency(),
					DEPOSIT,
					transaction.getText(),
					primaryAccount.getRegNumber(),
					primaryAccount.getAccountNumber());
			transaction.setId(keys.get(0));
		}

		public void visit(WithdrawTransaction transaction) throws SQLException {
			Money amount = transaction.getAmount();
			AccountNumber primaryAccount = transaction.getAccount().getAccountNumber();
			List<Integer> keys = helper.executeUpdateWithGeneratedKeys(
					"INSERT INTO Transaction(amount, currency, transaction_type, transaction_text, primary_reg_number, primary_account_number) VALUES (?, ?, ?, ?, ?, ?)",
					amount.getAmount(),
					amount.getCurrency(),
					WITHDRAWAL, transaction.getText(),
					primaryAccount.getRegNumber(),
					primaryAccount.getAccountNumber());
			transaction.setId(keys.get(0));
		}

		public void visit(TransferTransaction transaction) throws SQLException {
			Money amount = transaction.getAmount();
			AccountNumber primaryAccount = transaction.getAccount().getAccountNumber();
			AccountNumber secondaryAccount = transaction.getRecipient().getAccountNumber();
			List<Integer> keys = helper.executeUpdateWithGeneratedKeys(
					"INSERT INTO Transaction(amount, currency, transaction_type, transaction_text, primary_reg_number, primary_account_number, secondary_reg_number, secondary_account_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
					amount.getAmount(),
					amount.getCurrency(),
					TRANSFER,
					transaction.getText(),
					primaryAccount.getRegNumber(),
					primaryAccount.getAccountNumber(),
					secondaryAccount.getRegNumber(),
					secondaryAccount.getAccountNumber());
			transaction.setId(keys.get(0));
		}
	}
	
	private final TransactionCreator creator = new TransactionCreator();
	
	public Transaction create(Transaction transaction) throws SQLException {
		transaction.accept(creator);
		return transaction;
	}

	public Transaction read(int transactionId) throws SQLException {
		return helper.mapSingle(this::createTransaction, "SELECT * FROM Transaction WHERE transaction_id = ?", transactionId);
	}

	public List<Transaction> readAllFor(AccountNumber accountNumber) throws SQLException {
		return helper.map(this::createTransaction,
				"SELECT * FROM Transaction WHERE (primary_reg_number = ? AND primary_account_number = ?) OR (secondary_reg_number = ? AND secondary_account_number = ?)",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(), accountNumber.getRegNumber(), accountNumber.getAccountNumber());
	}

	public void deleteFor(Account account) throws SQLException {
		AccountNumber accountNumber = account.getAccountNumber();
		helper.executeUpdate("DELETE FROM Transaction WHERE (primary_reg_number = ? AND primary_account_number = ?) OR (secondary_reg_number = ? AND secondary_account_number = ?)",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(),accountNumber.getRegNumber(), accountNumber.getAccountNumber());
		
	}
}
