package dk.via.bank;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Customer;
import dk.via.bank.model.Money;
import dk.via.bank.model.transaction.Transaction;
import dk.via.bank.service.BranchAccountService;
import dk.via.bank.service.BranchCustomerService;
import dk.via.bank.service.BranchExchangeService;
import dk.via.bank.service.BranchTransactionService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.List;

public class BranchFacade extends UnicastRemoteObject implements Branch {
    private final BranchCustomerService customerService;
    private final BranchAccountService accountService;
    private final BranchTransactionService transactionService;
    private final BranchExchangeService exchangeService;

    protected BranchFacade(BranchCustomerService customerService, BranchAccountService accountService, BranchTransactionService transactionService, BranchExchangeService exchangeService) throws RemoteException {
        this.customerService = customerService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.exchangeService = exchangeService;
    }

    @Override
    public Customer createCustomer(String cpr, String name, String address) throws RemoteException {
        return customerService.createCustomer(cpr, name, address);
    }

    @Override
    public Customer getCustomer(String cpr) throws RemoteException {
        return customerService.getCustomer(cpr);
    }

    @Override
    public Account createAccount(Customer customer, String currency) throws RemoteException {
        return accountService.createAccount(customer, currency);
    }

    @Override
    public Account getAccount(AccountNumber accountNumber) throws RemoteException {
        return accountService.getAccount(accountNumber);
    }

    @Override
    public void cancelAccount(Account account) throws RemoteException {
        accountService.cancelAccount(account);
    }

    @Override
    public Collection<Account> getAccounts(Customer customer) throws RemoteException {
        return accountService.getAccountsFor(customer);
    }

    @Override
    public void execute(Transaction t) throws RemoteException {
        transactionService.execute(t);
    }

    @Override
    public Money exchange(Money amount, String targetCurrency) throws RemoteException {
        return exchangeService.exchange(amount, targetCurrency);
    }

    @Override
    public List<Transaction> getTransactionsFor(Account account) throws RemoteException {
        return transactionService.getTransactionsFor(account);
    }
}
