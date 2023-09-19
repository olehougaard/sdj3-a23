package dk.via.bank;

import dk.via.bank.dto.TransactionSpecification;
import dk.via.bank.grpc.*;
import dk.via.bank.interOp.GrpcFactory;
import dk.via.bank.model.Money;
import dk.via.bank.service.BranchAccountService;
import dk.via.bank.service.BranchCustomerService;
import dk.via.bank.service.BranchExchangeService;
import dk.via.bank.service.BranchTransactionService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@GrpcService
public class BranchServiceImpl extends BranchGrpc.BranchImplBase {
    private final BranchCustomerService customerService;
    private final BranchAccountService accountService;
    private final BranchTransactionService transactionService;
    private final BranchExchangeService exchangeService;

    public BranchServiceImpl(BranchCustomerService customerService, BranchAccountService accountService, BranchTransactionService transactionService, BranchExchangeService exchangeService) {
        this.customerService = customerService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.exchangeService = exchangeService;
    }

    @Override
    public void createCustomer(Customer request, StreamObserver<Customer> responseObserver) {
        dk.via.bank.model.Customer customer = customerService.createCustomer(request.getCpr(), request.getName(), request.getAddress());
        responseObserver.onNext(GrpcFactory.toGrpc(customer));
        responseObserver.onCompleted();
    }

    @Override
    public void getCustomer(CustomerRequest request, StreamObserver<Customer> responseObserver) {
        dk.via.bank.model.Customer customer = customerService.getCustomer(request.getCpr());
        responseObserver.onNext(GrpcFactory.toGrpc(customer));
        responseObserver.onCompleted();
    }

    @Override
    public void createAccount(CreateAccountRequest request, StreamObserver<Account> responseObserver) {
        dk.via.bank.model.Customer customer = GrpcFactory.fromGrpc(request.getCustomer());
        dk.via.bank.model.Account account = accountService.createAccount(customer, request.getCurrency());
        responseObserver.onNext(GrpcFactory.toGrpc(account));
        responseObserver.onCompleted();
    }

    @Override
    public void getAccount(AccountNumber request, StreamObserver<Account> responseObserver) {
        dk.via.bank.model.Account account = accountService.getAccount(GrpcFactory.fromGrpc(request));
        responseObserver.onNext(GrpcFactory.toGrpc(account));
        responseObserver.onCompleted();
    }

    @Override
    public void cancelAccount(Account request, StreamObserver<Confirmation> responseObserver) {
        accountService.cancelAccount(GrpcFactory.fromGrpc(request));
        responseObserver.onNext(Confirmation.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getAccounts(Customer request, StreamObserver<Accounts> responseObserver) {
        Collection<dk.via.bank.model.Account> accounts = accountService.getAccountsFor(GrpcFactory.fromGrpc(request));
        Accounts grpcAccounts = Accounts.newBuilder()
                .addAllAccount(GrpcFactory.toGrpcAccounts(accounts))
                .build();
        responseObserver.onNext(grpcAccounts);
        responseObserver.onCompleted();
    }

    @Override
    public void execute(Transaction request, StreamObserver<Confirmation> responseObserver) {
        dk.via.bank.model.Account account = accountService.getAccount(GrpcFactory.fromGrpc(request.getAccount().getAcctNumber()));
        dk.via.bank.model.Account recipient = null;
        if (request.getType().equals(TransactionSpecification.TRANSFER)) {
            recipient = accountService.getAccount(GrpcFactory.fromGrpc(request.getRecipient().getAcctNumber()));
        }
        transactionService.execute(GrpcFactory.fromGrpc(request, account, recipient));
        responseObserver.onNext(Confirmation.getDefaultInstance());
        responseObserver.onCompleted();

    }

    @Override
    public void exchange(ExchangeRequest request, StreamObserver<ExchangeResponse> responseObserver) {
        Money exchange = exchangeService.exchange(GrpcFactory.createMoney(request.getAmount10000(), request.getFromCurrency()), request.getToCurrency());
        responseObserver.onNext(GrpcFactory.createExchangeResponse(exchange));
        responseObserver.onCompleted();
    }

    @Override
    public void getTransactionsFor(Account request, StreamObserver<Transactions> responseObserver) {
        List<dk.via.bank.model.transaction.Transaction> transactionsFor = transactionService.getTransactionsFor(GrpcFactory.fromGrpc(request));
        Transactions grpcTransactions = Transactions.newBuilder()
                .addAllTransactions(GrpcFactory.toGrpcTransactions(transactionsFor))
                .build();
        responseObserver.onNext(grpcTransactions);
        responseObserver.onCompleted();
    }
}
