package dk.via.bank.data;

import dk.via.bank.model.transaction.*;
import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Money;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TransactionDataService extends UnicastRemoteObject implements TransactionData {
	private static final String DEPOSIT = "Deposit";
	private static final String TRANSFER = "Transfer";
	private static final String WITHDRAWAL = "Withdrawal";
	
	private static final long serialVersionUID = 1L;
	private final DatabaseHelper<Transaction> helper;
	private final AccountData accounts;
	
	public TransactionDataService(AccountData accounts, String jdbcURL, String username, String password) throws RemoteException {
		this.accounts = accounts;
		this.helper = new DatabaseHelper<>(jdbcURL, username, password);
	}

	private Account readAccount(ResultSet rs, String regNumberAttr, String acctNumberAttr) throws SQLException, RemoteException {
		return accounts.read(new AccountNumber(rs.getInt(regNumberAttr), rs.getLong(acctNumberAttr)));
	}
	private Transaction createTransaction(ResultSet rs) throws SQLException, RemoteException {
		Money amount = new Money(rs.getBigDecimal("amount"), rs.getString("currency"));
		String text = rs.getString("transaction_text");
		Account primary = readAccount(rs, "primary_reg_number", "primary_account_number");
		switch(rs.getString("transaction_type")) {
		case DEPOSIT:
			return new DepositTransaction(amount, primary, text);
		case WITHDRAWAL:
			return new WithdrawTransaction(amount, primary, text);
		case TRANSFER:
			Account secondaryAccount = readAccount(rs, "secondary_reg_number", "secondary_account_number");
			return new TransferTransaction(amount, primary, secondaryAccount, text);
		default:
			return null;
		}
	}

	private class TransactionCreator implements TransactionVisitor {
		@Override
		public void visit(DepositTransaction transaction) throws RemoteException {
			Money amount = transaction.getAmount();
			AccountNumber primaryAccount = transaction.getAccount().getAccountNumber();
			helper.executeUpdate("INSERT INTO Transaction(amount, currency, transaction_type, transaction_text, primary_reg_number, primary_account_number) VALUES (?, ?, ?, ?, ?, ?)", 
					amount.getAmount(), amount.getCurrency(), DEPOSIT, transaction.getText(), primaryAccount.getRegNumber(), primaryAccount.getAccountNumber());
		}

		@Override
		public void visit(WithdrawTransaction transaction) throws RemoteException {
			Money amount = transaction.getAmount();
			AccountNumber primaryAccount = transaction.getAccount().getAccountNumber();
			helper.executeUpdate("INSERT INTO Transaction(amount, currency, transaction_type, transaction_text, primary_reg_number, primary_account_number) VALUES (?, ?, ?, ?, ?, ?)", 
					amount.getAmount(), amount.getCurrency(), WITHDRAWAL, transaction.getText(), primaryAccount.getRegNumber(), primaryAccount.getAccountNumber());
		}

		@Override
		public void visit(TransferTransaction transaction) throws RemoteException {
			Money amount = transaction.getAmount();
			AccountNumber primaryAccount = transaction.getAccount().getAccountNumber();
			AccountNumber secondaryAccount = transaction.getRecipient().getAccountNumber();
			helper.executeUpdate("INSERT INTO Transaction(amount, currency, transaction_type, transaction_text, primary_reg_number, primary_account_number, secondary_reg_number, secondary_account_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", 
					amount.getAmount(), amount.getCurrency(), TRANSFER, transaction.getText(), primaryAccount.getRegNumber(), primaryAccount.getAccountNumber(), secondaryAccount.getRegNumber(), secondaryAccount.getAccountNumber());
		}
	}
	
	private final TransactionCreator creator = new TransactionCreator();
	
	@Override
	public void create(Transaction transaction) throws RemoteException {
		transaction.accept(creator);
	}

	@Override
	public Transaction read(int transactionId) throws RemoteException {
		return helper.mapSingle(this::createTransaction, "SELECT * FROM Transaction WHERE transaction_id = ?", transactionId);
	}

	@Override
	public List<Transaction> readAllFor(Account account) throws RemoteException {
		AccountNumber accountNumber = account.getAccountNumber();
		return helper.map(this::createTransaction,
				"SELECT * FROM Transaction WHERE (primary_reg_number = ? AND primary_account_number = ?) OR (secondary_reg_number = ? AND secondary_account_number = ?)",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(),accountNumber.getRegNumber(), accountNumber.getAccountNumber());
	}

	@Override
	public void deleteFor(Account account) throws RemoteException {
		AccountNumber accountNumber = account.getAccountNumber();
		helper.executeUpdate("DELETE FROM Transaction WHERE (primary_reg_number = ? AND primary_account_number = ?) OR (secondary_reg_number = ? AND secondary_account_number = ?)",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(),accountNumber.getRegNumber(), accountNumber.getAccountNumber());
		
	}
}
