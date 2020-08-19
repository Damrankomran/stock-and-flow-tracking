package com.damrankomran.poc_customer_api;

import com.damrankomran.poc_customer_api.model.Customer;
import com.damrankomran.poc_customer_api.repo.CustomerRepository;
import com.damrankomran.poc_customer_api.service.CustomerService;
import com.damrankomran.poc_customer_api.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    //Kontrol edeceğim sınıfı mock'luyorum.
    @Mock
    private CustomerRepository customerRepository;

    //Çalıştıracağım (gerçekten çağırmış olduğum) sınıfımdan nesne türetiyorum.
    private CustomerService customerService;

    @BeforeEach
    public void init() {
        customerService = new CustomerServiceImpl(customerRepository);
    }

    //-----------------findByID------------------------------
    @Test
    public void test_findByID_validID_returnsCustomer() {

        Long validID = 1L;
        Customer expected = new Customer();
        expected.setCustomerID(validID);
        expected.setName("Emre Aydın");

        //customerRepository.findByID validId ile çağrıldığında dönmesi gereken deger --> expected
        when(customerRepository.findById(validID)).thenReturn(Optional.of(expected));

        //actual nesnesi oluşturduk ve customerService'den findByID methodu ile validID parametresini gönderdik
        //customerService içerisinde customerRepository'nin findByID methodunu kullandığı için yukarıdaki işlem tetiklenecek.
        //actual'a bir cevap dönecek
        Optional<Customer> actual = customerService.findById(validID);

        verify(customerRepository, times(1)).findById(validID);

        //burada beklenen sonuç ile dönen sonucu karşılaştırıyoruz.
        actual.ifPresent(customer -> assertEquals(expected.getCustomerID(), customer.getCustomerID()));
    }

    @Test
    public void test_findByID_IdIsNull_returnsCustomer() {

        Exception exception = assertThrows(NullPointerException.class, () -> customerService.findById(null));

        assertEquals(exception.getClass(), NullPointerException.class);
    }


    //-----------------allCustomer------------------------------
    @Test
    public void test_allCustomer_returnsCustomerList() {

        List<Customer> expected = new ArrayList<>();

        Customer customer = new Customer();
        customer.setCustomerID(1L);
        customer.setName("Emre Aydın");

        Customer customer1 = new Customer();
        customer1.setCustomerID(2L);
        customer1.setName("Erdem Aydın");

        expected.add(customer);
        expected.add(customer1);

        when(customerRepository.findAll()).thenReturn(expected);

        List<Customer> actual = customerService.findAllCustomers();

        assertEquals(expected, actual);
        assertNotNull(actual);
    }

    //-----------------saveCustomer------------------------------

    @Test
    public void test_saveCustomer() {

        Customer customer = new Customer();
        customer.setName("Emre Aydın");

        when(customerRepository.save(customer)).thenReturn(null);

        customerService.saveCustomer(customer);

        verify(customerRepository, times(1)).save(customer);
    }

    //-----------------updateCustomer------------------------------
    @Test
    public void test_updateCustomer() {

        Customer customer = new Customer();
        customer.setName("Emre Aydın");

        when(customerRepository.save(customer)).thenReturn(null);

        customerService.updateCustomer(customer);

        verify(customerRepository, times(1)).save(customer);
    }

    //-----------------deleteCustomer------------------------------

    @Test
    public void test_deleteCustomerByID() {

        Customer customer = new Customer();
        customer.setCustomerID(1L);

        customerService.deleteCustomerById(customer.getCustomerID());

        verify(customerRepository, times(1)).deleteById(customer.getCustomerID());
    }

    @Test
    public void test_deleteCustomer_idIsNull(){

        Exception exception = assertThrows(NullPointerException.class, () -> customerService.deleteCustomerById(null));

        assertEquals(NullPointerException.class, exception.getClass());
    }

    //-----------------deleteAllCustomer------------------------------
    @Test
    public void test_deleteAllCustomer() {

       customerService.deleteAllCustomers();

       verify(customerRepository,times(1)).deleteAll();
    }

}


