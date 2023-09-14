package dk.via.bank.service;

import dk.via.bank.data.CustomerData;
import dk.via.bank.model.Customer;

import java.rmi.RemoteException;

public class BranchCustomerService {
    private final CustomerData customerData;

    public BranchCustomerService(CustomerData customerData) {
        this.customerData = customerData;
    }

    public Customer createCustomer(String cpr, String name, String address) {
        return customerData.create(cpr, name, address);
    }

    public Customer getCustomer(String cpr) {
        return customerData.read(cpr);
    }
}
