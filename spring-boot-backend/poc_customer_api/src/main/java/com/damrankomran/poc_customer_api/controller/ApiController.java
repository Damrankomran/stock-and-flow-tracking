package com.damrankomran.poc_customer_api.controller;

import com.damrankomran.poc_customer_api.model.Customer;
import com.damrankomran.poc_customer_api.service.CustomerService;
import com.damrankomran.poc_customer_api.util.CustomErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final CustomerService customerService; //Service which will do all data retrieval/manipulation work

    // -------------------Retrieve All Customers---------------------------------------------
    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCustomer() {
        log.info("Get all customer");
        try {
            List<Customer> customers = customerService.findAllCustomers();
            if (customers.isEmpty()) {
                log.error("Customers not found");
                return new ResponseEntity<>(new CustomErrorType("Customers not found"), HttpStatus.NOT_FOUND);
            }
            log.info("Customers found!");
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // -------------------Retrieve Single Customer------------------------------------------
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCustomer(@PathVariable("id") long id) {
        log.info("Fetching Customer with id {}", id);
        try {
            validateID(id);
            Optional<Customer> customer = customerService.findById(id);
            if (!customer.isPresent()) {
                log.error("Customer with id {} not found.", id);
                return new ResponseEntity<>(new CustomErrorType("User with id " + id
                        + " not found"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(customer.get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // -------------------Create a Customer-------------------------------------------
    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder) {
        log.info("Creating Customer : {}", customer);
        try {
            validateRequestBody(customer);
            if (customerService.isCustomerExist(customer)) {
                log.error("Unable to create. A customer that matches this information already exists");
                return new ResponseEntity<>(new CustomErrorType("Unable to create. A customer that matches this" +
                        " information already exists"), HttpStatus.CONFLICT);
            }
            customerService.saveCustomer(customer);
            log.info("Creating Customer Successful!");

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/api/customer/{id}").buildAndExpand(customer.getCustomerID()).toUri());

            return new ResponseEntity<>(customer, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ------------------- Update a User ------------------------------------------------
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
        log.info("Updating Customer");
        log.info("Customer Searching");
        try {
            validateID(id);
            validateRequestBody(customer);
            Optional<Customer> currentCustomer = customerService.findById(id);

            if (!currentCustomer.isPresent()) {
                log.error("Unable to update. User with id {} not found.", id);
                return new ResponseEntity<>(new CustomErrorType("Unable to update. Customer with id " +
                        id + " not found."),
                        HttpStatus.NOT_FOUND);
            }

            if (customerService.isCustomerExist(customer)) {
                log.error("Update failed. Such a user exists or you have not made any updates to the registry.");
                return new ResponseEntity<>(new CustomErrorType("Update failed. Such a user exists or you have not made any updates to the registry."),
                        HttpStatus.BAD_REQUEST);
            }

            //Gelen requestBody'de customerID'belirtilmemiş olabilir. Onun için currentCustomer'daki degeri alıyoruz.
            customer.setCustomerID(currentCustomer.get().getCustomerID());
            customerService.updateCustomer(customer);

            log.info("Current Customer : {}", currentCustomer.get());
            log.info("Update Customer : {}", customer);

            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    // ------------------- Delete a Customer-----------------------------------------

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") long id) {
        log.info("Fetching & Deleting Customer with id {}", id);
        try {
            validateID(id);
            // isPresent -> null kontorülüdür. Boş ise "false", Dolu ise" true" değerini döndürür.
            Optional<Customer> currentCustomer = customerService.findById(id);

            if (!currentCustomer.isPresent()) {
                log.error("Unable to update. User with id {} not found.", id);
                return new ResponseEntity<>(new CustomErrorType("Unable to update. Customer with id " + id + " not found."),
                        HttpStatus.NOT_FOUND);
            }
            log.info("Delete customer {}", currentCustomer.get());
            customerService.deleteCustomerById(id);

            return new ResponseEntity<Customer>(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    // ------------------- Delete All Customers-----------------------------

    @RequestMapping(value = "/customer", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAllCustomers() {
        log.info("Deleting All Customers");
        try {
            customerService.deleteAllCustomers();
            return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ------------------- validate ID -----------------------------
    private void validateID(@NonNull Long ID) {
        if (Long.valueOf(0L).equals(ID)) {
            throw new RuntimeException("customerID can not be 0!");
        }
        if (ID < 0) {
            throw new RuntimeException("customerID can not be negative value!");
        }
    }
    // ------------------- validate RequestBody -----------------------------
    private void validateRequestBody(Customer customer){
        log.info("validateRequestBodySaveCustomer");
        Optional<Long> optionalCustomerID = Optional.ofNullable(customer.getCustomerID());
        if (optionalCustomerID.isPresent()) {
            throw new RuntimeException("customerID is generated value. It cannot be sent within the Request Body");
        }
    }


}
