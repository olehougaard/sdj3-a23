package dk.via.bank.adapter;

import dk.via.bank.data.TransactionData;
import dk.via.bank.dto.TransactionSpecification;
import dk.via.bank.model.Account;
import dk.via.bank.model.transaction.Transaction;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

public class TransactionRestAdapter implements TransactionData {
    private final String endpoint;
    private final RestTemplate restTemplate;

    public TransactionRestAdapter(String endpoint) {
        this.endpoint = endpoint + "/" + "accounts";
        this.restTemplate = new RestTemplate();
    }

    private String transactionURI(Account account) {
        return endpoint + "/" + account.getAccountNumber() + ("/transactions");
    }

    @Override
    public Transaction read(int transactionId, Account account) throws RemoteException {
        try {
            return restTemplate.getForObject(transactionURI(account) + "/" + transactionId, Transaction.class);
        } catch (RestClientException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public List<Transaction> readAllFor(Account account) throws RemoteException {
        try {
            TransactionSpecification[] transactions = restTemplate.getForObject(transactionURI(account), TransactionSpecification[].class);
            if (transactions == null) throw new RemoteException("No response from " + transactionURI(account));
            return Arrays.stream(transactions).map(s -> s.toTransaction(account)).toList();
        } catch (RestClientException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public Transaction create(Transaction transaction) throws RemoteException {
        try {
            TransactionSpecification spec = TransactionSpecification.from(transaction);
            Account account1 = transaction.getAccount();
            TransactionSpecification response = restTemplate.postForEntity(transactionURI(account1), spec, TransactionSpecification.class).getBody();
            if (response == null) throw new RemoteException("No response from " + transactionURI(account1));
            return response.toTransaction(account1);
        } catch (RestClientException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void delete(int transactionId, Account account) throws RemoteException {
        try {
            restTemplate.delete(transactionURI(account) + "/" + transactionId);
        } catch (RestClientException e) {
            throw new RemoteException(e.getMessage());
        }
    }
}
