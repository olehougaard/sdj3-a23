package dk.via.bank.adapter;

import dk.via.bank.data.AccountData;
import dk.via.bank.dto.AccountSpecification;
import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Customer;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;

@Component
public class AccountRestAdapter implements AccountData {
    private final String endpoint;
    private final RestTemplate restTemplate;

    @Autowired
    public AccountRestAdapter(@Value("${hq.endpoint}") String endpoint) {
        this.endpoint = endpoint + "accounts";
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Account create(int regNumber, Customer customer, String currency) {
        AccountSpecification spec = new AccountSpecification(regNumber, currency, customer.getCpr());
        return restTemplate.postForEntity(endpoint, spec, Account.class).getBody();
    }

    @Override
    public Account read(AccountNumber accountNumber) {
        return restTemplate.getForObject(endpoint + "/" + accountNumber, Account.class);
    }

    @Override
    public Collection<Account> readAccountsFor(Customer customer) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint);
        builder.queryParam("cpr", customer.getCpr());
        Traverson traverson = new Traverson(builder.build(0), MediaTypes.HAL_JSON);
        CollectionModel<Account> accountsModel = traverson.follow()
                .toObject(new TypeReferences.CollectionModelType<Account>() {});
        if (accountsModel == null) throw new RuntimeException("No response from " + builder.toUriString());
        return accountsModel.getContent();
    }

    @Override
    public void update(Account account) {
        restTemplate.put(endpoint + "/" + account.getAccountNumber(), account);
    }

    @Override
    public void delete(Account account)  {
        restTemplate.delete(endpoint + "/" + account.getAccountNumber());
    }
}
