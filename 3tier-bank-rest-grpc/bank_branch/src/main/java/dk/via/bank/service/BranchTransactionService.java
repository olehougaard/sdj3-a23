package dk.via.bank.service;

import java.rmi.RemoteException;
import java.util.List;

import dk.via.bank.data.AccountData;
import dk.via.bank.data.ExchangeRateData;
import dk.via.bank.data.TransactionData;
import dk.via.bank.model.*;
import dk.via.bank.model.transaction.*;

public class BranchTransactionService implements TransactionVisitor<RuntimeException> {
	private final AccountData accountData;
	private final TransactionData transactionData;
	private final ExchangeRateData exchangeRateData;
	
	public BranchTransactionService(AccountData accountData, TransactionData transactionData, ExchangeRateData exchangeData) {
		this.accountData = accountData;
		this.transactionData = transactionData;
		this.exchangeRateData = exchangeData;
	}

	public void execute(Transaction t) {
		t.accept(this);
		transactionData.create(t);
	}
	
	private Money translateToSettledCurrency(Money amount, Account account) {
		if (!amount.getCurrency().equals(account.getSettledCurrency())) {
			ExchangeRate rate = exchangeRateData.getExchangeRate(amount.getCurrency(), account.getSettledCurrency());
			amount = rate.exchange(amount);
		}
		return amount;
	}

	@Override
	public void visit(DepositTransaction transaction) {
		Account account = transaction.getAccount();
		Money amount = transaction.getAmount();
		amount = translateToSettledCurrency(amount, account);
		account.deposit(amount);
		accountData.update(account);
	}
	
	@Override
	public void visit(WithdrawTransaction transaction) {
		Account account = transaction.getAccount();
		Money amount = transaction.getAmount();
		amount = translateToSettledCurrency(amount, account);
		if (amount.getAmount().doubleValue() <= account.getBalance().getAmount().doubleValue()) {
			account.withdraw(amount);
			accountData.update(account);
		} else {
			throw new IllegalStateException("Insufficient funds");
		}
	}
	
	@Override
	public void visit(TransferTransaction transaction) {
		visit(transaction.getDepositTransaction());
		visit(transaction.getWithdrawTransaction());
	}

	public List<Transaction> getTransactionsFor(Account primaryAccount) {
		return transactionData.readAllFor(primaryAccount);
	}
}
