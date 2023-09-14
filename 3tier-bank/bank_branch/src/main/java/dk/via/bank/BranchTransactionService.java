package dk.via.bank;

import java.rmi.RemoteException;
import java.util.List;

import dk.via.bank.data.AccountData;
import dk.via.bank.data.ExchangeRateData;
import dk.via.bank.data.TransactionData;
import dk.via.bank.model.*;
import dk.via.bank.model.transaction.*;

public class BranchTransactionService implements TransactionVisitor {
	private final AccountData accountData;
	private final TransactionData transactionData;
	private final ExchangeRateData exchangeRateData;
	
	public BranchTransactionService(AccountData accountData, TransactionData transactionData, ExchangeRateData exchangeData) throws RemoteException {
		this.accountData = accountData;
		this.transactionData = transactionData;
		this.exchangeRateData = exchangeData;
	}

	public void execute(Transaction t) throws RemoteException {
		t.accept(this);
		transactionData.create(t);
	}
	
	private Money translateToSettledCurrency(Money amount, Account account) throws RemoteException {
		if (!amount.getCurrency().equals(account.getSettledCurrency())) {
			ExchangeRate rate = exchangeRateData.getExchangeRate(amount.getCurrency(), account.getSettledCurrency());
			amount = rate.exchange(amount);
		}
		return amount;
	}

	@Override
	public void visit(DepositTransaction transaction) throws RemoteException {
		Account account = transaction.getAccount();
		Money amount = transaction.getAmount();
		amount = translateToSettledCurrency(amount, account);
		account.deposit(amount);
		accountData.update(account);
	}
	
	@Override
	public void visit(WithdrawTransaction transaction) throws RemoteException {
		Account account = transaction.getAccount();
		Money amount = transaction.getAmount();
		amount = translateToSettledCurrency(amount, account);
		if (amount.getAmount().doubleValue() <= account.getBalance().getAmount().doubleValue()) {
			account.withdraw(amount);
			accountData.update(account);
		} else {
			throw new RemoteException("Insufficient funds");
		}
	}
	
	@Override
	public void visit(TransferTransaction transaction) throws RemoteException {
		visit(transaction.getDepositTransaction());
		visit(transaction.getWithdrawTransaction());
	}

	public List<Transaction> getTransactionsFor(Account primaryAccount) throws RemoteException {
		return transactionData.readAllFor(primaryAccount);
	}
}
