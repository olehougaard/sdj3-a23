package dk.via.bank;

import dk.via.bank.adapter.AccountRestAdapter;
import dk.via.bank.adapter.CustomerRestAdapter;
import dk.via.bank.adapter.ExchangeRateRestAdapter;
import dk.via.bank.adapter.TransactionRestAdapter;
import dk.via.bank.data.AccountData;
import dk.via.bank.data.CustomerData;
import dk.via.bank.data.ExchangeRateData;
import dk.via.bank.data.TransactionData;
import dk.via.bank.service.BranchAccountService;
import dk.via.bank.service.BranchCustomerService;
import dk.via.bank.service.BranchExchangeService;
import dk.via.bank.service.BranchTransactionService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class RunBranch {
	public static final int REG_NUMBER = 4711;

	public static void main(String[] args) throws Exception {
		String uri = "http://localhost:8080/";

		CustomerData customerData = new CustomerRestAdapter(uri);
		AccountData accountData = new AccountRestAdapter(uri);
		ExchangeRateData exchangeData = new ExchangeRateRestAdapter(uri);
		TransactionData transactionData = new TransactionRestAdapter(uri);

		BranchCustomerService customerService = new BranchCustomerService(customerData);
		BranchAccountService accountService = new BranchAccountService(REG_NUMBER, accountData);
		BranchTransactionService transactionService = new BranchTransactionService(accountData, transactionData, exchangeData);
		BranchExchangeService exchangeService = new BranchExchangeService(exchangeData);

		BranchServiceImpl branchService = new BranchServiceImpl(customerService, accountService, transactionService, exchangeService);
		Server server = ServerBuilder
				.forPort(9090)
				.addService(branchService)
				.build();
		server.start();
		server.awaitTermination();
	}
}
