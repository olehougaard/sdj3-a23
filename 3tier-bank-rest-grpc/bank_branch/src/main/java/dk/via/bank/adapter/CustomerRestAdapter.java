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
    public Customer create(String cpr, String name, String address) throws RemoteException {
        try {
            Customer customer = new Customer(cpr, name, address);
            return restTemplate.postForObject(endpoint, customer, Customer.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public Customer read(String cpr) throws RemoteException {
        try {
            return restTemplate.getForEntity(endpoint + "/" + cpr, Customer.class).getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void update(Customer customer) throws RemoteException {
        try {
            restTemplate.put(endpoint + "/" + customer.getCpr(), customer);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void delete(Customer customer) throws RemoteException {
        try {
            restTemplate.delete(endpoint + "/" + customer.getCpr());
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }
}
