package dk.via.bank;

import dk.via.bank.grpc.*;
import io.grpc.stub.StreamObserver;

public class BranchServiceImpl extends BranchGrpc.BranchImplBase {
    @Override
    public void createCustomer(Customer request, StreamObserver<Customer> responseObserver) {
        super.createCustomer(request, responseObserver);
    }

    @Override
    public void getCustomer(CustomerRequest request, StreamObserver<Customer> responseObserver) {
        super.getCustomer(request, responseObserver);
    }

    @Override
    public void createAccount(CreateAccountRequest request, StreamObserver<Account> responseObserver) {
        super.createAccount(request, responseObserver);
    }

    @Override
    public void getAccount(AccountNumber request, StreamObserver<Account> responseObserver) {
        super.getAccount(request, responseObserver);
    }

    @Override
    public void cancelAccount(Account request, StreamObserver<Confirmation> responseObserver) {
        super.cancelAccount(request, responseObserver);
    }

    @Override
    public void getAccounts(Customer request, StreamObserver<Accounts> responseObserver) {
        super.getAccounts(request, responseObserver);
    }

    @Override
    public void execute(Transaction request, StreamObserver<Confirmation> responseObserver) {
        super.execute(request, responseObserver);
    }

    @Override
    public void exchange(ExchangeRequest request, StreamObserver<ExchangeResponse> responseObserver) {
        super.exchange(request, responseObserver);
    }

    @Override
    public void getTransactionsFor(Account request, StreamObserver<Transactions> responseObserver) {
        super.getTransactionsFor(request, responseObserver);
    }
}
