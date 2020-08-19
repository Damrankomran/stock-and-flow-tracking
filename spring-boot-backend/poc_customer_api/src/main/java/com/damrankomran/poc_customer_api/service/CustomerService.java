package com.damrankomran.poc_customer_api.service;

import com.damrankomran.poc_customer_api.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Optional<Customer> findById(Long customerID);

    void saveCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomerById(Long customerID);

    void deleteAllCustomers();

    List<Customer> findAllCustomers();

    boolean isCustomerExist(Customer customer);

}
