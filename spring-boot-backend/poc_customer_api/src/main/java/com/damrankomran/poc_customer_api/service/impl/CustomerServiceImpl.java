package com.damrankomran.poc_customer_api.service.impl;

import com.damrankomran.poc_customer_api.model.Customer;
import com.damrankomran.poc_customer_api.repo.CustomerRepository;
import com.damrankomran.poc_customer_api.service.CustomerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("customerService")
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public Optional<Customer> findById(@NonNull Long customerID) {
        return customerRepository.findById(customerID);
    }

    //JPA otomatik olarak bir ID atadığı için ID kontrolu yapılmıyor
    /*
    Yeni bir müşteri eklendiğinde, önbellekte yer alan müşteri listesini güncellemek isteriz.
    Bu yüzden, zaman aşımı belirtilmeyen önbelleği güncel tutmak için başka bir anotasyona daha ihtiyaç duyarız:
     @CacheEvict. Bu anotasyon ile servis sınıfındaki save() metodu her çağrıldığında, anahtarı customersCache olan önbelleği boşaltması gerektiğini belirtiriz.
     Herhangi bir koşul gözetmeksizin, tüm verilerini silmesini istediğimiz için de allEntries değerini true yapmalıyız.
     */
    @CacheEvict(value = "findAllCustomers", allEntries = true)
    public void saveCustomer(Customer customer){
        customerRepository.save(customer);
    }

    //Controller kısmında gönderilen customer'ın var olup olmadığı kontrol ediliyor. O yüzden ID kontrol
    @CacheEvict(value = "findAllCustomers", allEntries = true)
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @CacheEvict(value = "findAllCustomers", allEntries = true)
    public void deleteCustomerById(@NonNull Long customerID) {
        customerRepository.deleteById(customerID);
    }

    @CacheEvict(value = "findAllCustomers", allEntries = true)
    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }

    @Cacheable(value = "findAllCustomers")
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    public boolean isCustomerExist(Customer customer) {
        //Bütün kullancılarda name,age,adress,tel bilgileri eşit olan bir customer bulursa true dönecek
        List<Customer> customers = findAllCustomers();
        for(Customer obj : customers){
            if(obj.equals(customer)){
                return true;
            }
        }
        return false;
    }

}
