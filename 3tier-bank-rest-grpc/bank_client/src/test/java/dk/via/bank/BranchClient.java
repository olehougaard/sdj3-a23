package dk.via.bank;

import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Customer;
import dk.via.bank.model.Money;
import dk.via.bank.model.transaction.AbstractTransaction;
import dk.via.bank.grpc.BranchGrpc;
import dk.via.bank.grpc.ExchangeResponse;
import dk.via.bank.interOp.GrpcFactory;
import dk.via.bank.model.transaction.Transaction;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BranchClient implements Branch {
    private String host;
    private int port;

    public BranchClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void execute(Consumer<BranchGrpc.BranchBlockingStub> consumer) {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        try {
            BranchGrpc.BranchBlockingStub stub = BranchGrpc.newBlockingStub(managedChannel);
            consumer.accept(stub);
        } finally {
            managedChannel.shutdown();
        }
    }

    private<R> R apply(Function<BranchGrpc.BranchBlockingStub, R> consumer) {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        try {
            BranchGrpc.BranchBlockingStub stub = BranchGrpc.newBlockingStub(managedChannel);
            return consumer.apply(stub);
        } finally {
            managedChannel.shutdown();
        }
    }

    @Override
    public Customer createCustomer(String cpr, String name, String address) {
        return apply(stub -> GrpcFactory.fromGrpc(stub.createCustomer(GrpcFactory.createCustomer(cpr, name, address))));
    }

    @Override
    public Customer getCustomer(String cpr) {
        return apply(stub -> GrpcFactory.fromGrpc(stub.getCustomer(GrpcFactory.createCustomerRequest(cpr))));
    }

    @Override
    public Account createAccount(Customer customer, String currency) {
        return apply(stub -> GrpcFactory.fromGrpc(stub.createAccount(GrpcFactory.createAccountRequest(customer, currency))));
    }

    @Override
    public Account getAccount(AccountNumber accountNumber) {
        return apply(stub -> GrpcFactory.fromGrpc(stub.getAccount(GrpcFactory.toGrpc(accountNumber))));
    }

    @Override
    public void cancelAccount(Account account) {
        execute(stub -> stub.cancelAccount(GrpcFactory.toGrpc(account)));
    }

    @Override
    public Collection<Account> getAccounts(Customer customer) {
        return apply(stub -> GrpcFactory.fromGrpcAccounts(stub.getAccounts(GrpcFactory.toGrpc(customer))));
    }

    @Override
    public void execute(Transaction t) {
        execute(stub -> stub.execute(GrpcFactory.toGrpc(t)));
    }

    @Override
    public Money exchange(Money amount, String targetCurrency) {
        return apply(stub -> {
            ExchangeResponse exchange = stub.exchange(GrpcFactory.createExchangeRequest(amount, targetCurrency));
            return GrpcFactory.createMoney(exchange.getAmount100(), exchange.getCurrency());
        });
    }

    @Override
    public List<Transaction> getTransactionsFor(Account account) {
        return apply(stub -> GrpcFactory.fromGrpcTransactions(stub.getTransactionsFor(GrpcFactory.toGrpc(account))));
    }
}
