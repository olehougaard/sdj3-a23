package dk.via.bank;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import dk.via.bank.data.*;

public class RunBranch {
	public static final int REG_NUMBER = 4711;

	public static void main(String[] args) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(1099);
		HeadQuarters hq = (HeadQuarters) registry.lookup("HQ");
		CustomerData customerData = hq.getCustomerData();
		AccountData accountData = hq.getAccountData();
		ExchangeRateData exchangeData = hq.getExchangeData();
		TransactionData transactionData = hq.getTransactionData();

		BranchCustomerService customerService = new BranchCustomerService(customerData);
		BranchAccountService accountService = new BranchAccountService(REG_NUMBER, accountData);
		BranchTransactionService transactionService = new BranchTransactionService(accountData, transactionData, exchangeData);
		BranchExchangeService exchangeService = new BranchExchangeService(exchangeData);

		BranchFacade branch1 = new BranchFacade(customerService, accountService, transactionService, exchangeService);

		Registry branchRegistry = LocateRegistry.createRegistry(8099);
		branchRegistry.rebind("Branch 1", branch1);
		System.out.println("Branch started");
	}
}
