package dk.via.bank.adapter;

import dk.via.bank.data.CustomerData;
import dk.via.bank.model.Customer;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.rmi.RemoteException;

public class CustomerRestAdapter implements CustomerData {
    private final String endpoint;
    private final RestTemplate restTemplate;

    public CustomerRestAdapter(String endpoint) {
        this.endpoint = endpoint + "customers";
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Customer create(String cpr, String name, String address) {
        Customer customer = new Customer(cpr, name, address);
        return restTemplate.postForObject(endpoint, customer, Customer.class);
    }

    @Override
    public Customer read(String cpr) {
        return restTemplate.getForEntity(endpoint + "/" + cpr, Customer.class).getBody();
    }

    @Override
    public void update(Customer customer) {
        restTemplate.put(endpoint + "/" + customer.getCpr(), customer);
    }

    @Override
    public void delete(Customer customer) {
        restTemplate.delete(endpoint + "/" + customer.getCpr());
    }
}
