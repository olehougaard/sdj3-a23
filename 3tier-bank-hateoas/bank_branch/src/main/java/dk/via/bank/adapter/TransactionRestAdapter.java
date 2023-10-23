package dk.via.bank.adapter;

import dk.via.bank.data.TransactionData;
import dk.via.bank.dto.TransactionSpecification;
import dk.via.bank.model.Account;
import dk.via.bank.model.transaction.Transaction;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TransactionRestAdapter implements TransactionData {
    private final String endpoint;
    private final RestTemplate restTemplate;

    public TransactionRestAdapter(String endpoint) {
        this.endpoint = endpoint + "/" + "accounts";
        this.restTemplate = new RestTemplate();
    }

    private String transactionURI(Account account) {
        return endpoint + "/" + account.getAccountNumber() + "/transactions";
    }

    @Override
    public Transaction read(int transactionId, Account account) throws RemoteException {
        try {
            return restTemplate.getForObject(transactionURI(account) + "/" + transactionId, Transaction.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public List<Transaction> readAllFor(Account account) throws RemoteException {
        try {
            URI uri = UriComponentsBuilder.fromUriString(endpoint + "/" + account.getAccountNumber()).build(0);
            Traverson traverson = new Traverson(uri, MediaTypes.HAL_JSON);
            CollectionModel<TransactionSpecification> ts = traverson
                    .follow("$._links.transactions.href")
                    .toObject(new TypeReferences.CollectionModelType<TransactionSpecification>() {});
            if (ts == null) throw new RemoteException("No response from " + transactionURI(account));
            Collection<TransactionSpecification> transactions = ts.getContent();
            return transactions.stream().map(s -> s.toTransaction(account)).toList();
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void delete(int transactionId, Account account) throws RemoteException {
        try {
            restTemplate.delete(transactionURI(account) + "/" + transactionId);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }
}
