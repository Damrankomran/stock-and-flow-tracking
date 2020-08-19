package com.damrankomran.poc_customer_api;

import com.damrankomran.poc_customer_api.controller.ApiController;
import com.damrankomran.poc_customer_api.model.Customer;
import com.damrankomran.poc_customer_api.repo.CustomerRepository;
import com.damrankomran.poc_customer_api.service.CustomerService;
import com.damrankomran.poc_customer_api.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiControllerTest {

    @Mock
    private CustomerRepository customerRepository;

    private ApiController apiController;

    @BeforeEach
    public void init() {
        CustomerService customerService = new CustomerServiceImpl(customerRepository);
        apiController = new ApiController(customerService);
    }

    // -------------------Get All Customers---------------------------------------------
    @Test
    public void test_getAllCustomer_returnsCustomers() {

        Customer customer = new Customer();
        customer.setCustomerID(1L);
        customer.setName("Emre Aydın");

        Customer customer1 = new Customer();
        customer1.setCustomerID(2L);
        customer1.setName("Erdem Aydın");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer));

        ResponseEntity<?> responseEntity = apiController.getAllCustomer();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void test_getAllCustomer_returnsNotFound() {

        ResponseEntity<?> responseEntity = apiController.getAllCustomer();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    // -------------------Get Customer---------------------------------------------

    @Test
    public void test_getCustomer_validID_returnsCustomer() {

        Long validID = 1L;
        Customer customer = new Customer();
        customer.setCustomerID(validID);
        customer.setName("Emre Aydın");

        when(customerRepository.findById(validID)).thenReturn(Optional.of(customer));

        ResponseEntity<?> responseEntity = apiController.getCustomer(validID);
        assertEquals(customer,responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    public void test_getCustomer_validID_returnsNotFound() {

        Long validID = 1L;

        ResponseEntity<?> responseEntity = apiController.getCustomer(validID);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    // -------------------Save Customer---------------------------------------------
    @Test
    public void test_saveCustomer_returnsCustomer(){
        Customer customer = new Customer();
        customer.setName("Hugo");

        ResponseEntity<?> responseEntity = apiController.createCustomer(customer, UriComponentsBuilder.newInstance());
        verify(customerRepository,times(1)).save(customer);
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals(customer,responseEntity.getBody());
    }

    @Test
    public void test_saveCustomer_customerIdIsNotNull_returnsError(){
        Customer customer = new Customer();
        customer.setCustomerID(1L);
        customer.setName("Hugo");

        ResponseEntity<?> responseEntity = apiController.createCustomer(customer, UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    @Test
    public void test_saveCustomer_isCustomerExist(){

        Customer customer1 = new Customer();
        customer1.setCustomerID(1L);
        customer1.setName("Emre Aydın");

        Customer customer2 = new Customer();
        customer2.setCustomerID(2L);
        customer2.setName("Erdem Aydın");

        Customer customer = new Customer();
        customer.setName("Emre Aydın");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1,customer2));

        ResponseEntity<?> responseEntity = apiController.createCustomer(customer, UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    // -------------------Update Customer---------------------------------------------
    @Test
    public void test_updateCustomer_validID_returnsCustomer(){
        Long validID = 1L;
        Customer currentCustomer = new Customer();
        currentCustomer.setCustomerID(validID);
        currentCustomer.setName("Emre Aydın");
        currentCustomer.setAddress("Gaziosmanpaşa");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("Emre Aydın");
        updatedCustomer.setAddress("Küçükköy");

        when(customerRepository.findById(validID)).thenReturn(Optional.of(currentCustomer));

        ResponseEntity<?> responseEntity = apiController.updateCustomer(validID, updatedCustomer);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedCustomer,responseEntity.getBody());
    }

    @Test
    public void test_updateCustomer_validID_customerNotFound(){
        Long validID = 1L;
        Customer customer = new Customer();
        customer.setName("Emre");

        ResponseEntity<?> responseEntity = apiController.updateCustomer(validID, customer);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void test_updateCustomer_isCustomerExist(){
        Customer customer1 = new Customer();
        customer1.setCustomerID(1L);
        customer1.setName("Emre Aydın");

        Customer customer2 = new Customer();
        customer2.setCustomerID(2L);
        customer2.setName("Erdem Aydın");

        Long validID = 1L;
        Customer customer = new Customer();
        customer.setName("Emre Aydın");

        // customerRepository.findAll() çağrıldığında customer1 ve customer2 içeren bir liste döndürmesini istedik
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1,customer2));
        // customerRepository.findID() çağrıldığında customer1 döndürmesini istedik
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        ResponseEntity<?> responseEntity = apiController.updateCustomer(validID, customer);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void test_updateCustomer_customerIdIsNotNull_returnsError(){
        Customer customer = new Customer();
        customer.setCustomerID(1L);
        customer.setName("Hugo");

        ResponseEntity<?> responseEntity = apiController.updateCustomer(1L,customer);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }
    // -------------------Delete Customer---------------------------------------------
    @Test
    public void test_deleteCustomer_validID(){
        Long validID = 1L;
        Customer customer = new Customer();
        customer.setCustomerID(validID);

        when(customerRepository.findById(validID)).thenReturn(Optional.of(customer));

        ResponseEntity<?> responseEntity = apiController.deleteCustomer(validID);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void test_deleteCustomer_validID_customerNotFound(){
        Long validID = 1L;
        Customer customer = new Customer();
        customer.setCustomerID(validID);

        when(customerRepository.findById(validID)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = apiController.deleteCustomer(validID);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    //-----------------delete All Customer------------------------------
    @Test
    public void test_deleteAllCustomer(){

        ResponseEntity<?> responseEntity = apiController.deleteAllCustomers();

        verify(customerRepository, times(1)).deleteAll();

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

}
