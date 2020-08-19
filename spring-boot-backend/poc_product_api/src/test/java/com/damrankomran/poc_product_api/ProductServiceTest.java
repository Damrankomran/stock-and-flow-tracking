package com.damrankomran.poc_product_api;

import com.damrankomran.poc_product_api.model.Product;
import com.damrankomran.poc_product_api.repo.ProductRepository;
import com.damrankomran.poc_product_api.service.ProductService;
import com.damrankomran.poc_product_api.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    public void init() {
        productService = new ProductServiceImpl(productRepository);
    }

    //---------------------------------- findByID ----------------------------------------
    @Test
    public void test_findByID_validID_returnsProduct() {

        Long validID = 1L;
        Product expected = new Product();
        expected.setProductID(validID);
        expected.setName("Product 1");

        when(productRepository.findById(expected.getProductID())).thenReturn(java.util.Optional.of(expected));

        Optional<Product> actual = productService.findByID(validID);

        actual.ifPresent(product -> assertEquals(expected, actual.get()));

        verify(productRepository, times(1)).findById(validID);
    }

    @Test
    public void test_findByID_validIdIsNull_returnsException() {

        Exception exception = assertThrows(NullPointerException.class, () -> productService.findByID(null));

        assertEquals(NullPointerException.class, exception.getClass());
    }

    //---------------------------------- Find All Products ----------------------------------------
    @Test
    public void test_findAllProducts_returnsProducts() {

        Product product1 = new Product();
        product1.setProductID(1L);
        product1.setName("Product1");

        Product product2 = new Product();
        product2.setProductID(2L);
        product2.setName("Product2");

        List<Product> expected = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> actual = productService.findAllProduct();

        assertEquals(expected, actual);

        verify(productRepository, times(1)).findAll();
    }

    //---------------------------------- findByName ----------------------------------------

    @Test
    public void test_findByName_validName_returnsProduct() {

        String validName = "Emre";
        Product expected = new Product();
        expected.setProductID(1L);
        expected.setName("Emre");

        when(productRepository.findByName(validName)).thenReturn(expected);

        Optional<Product> actual = Optional.ofNullable(productService.findByName(validName));

        actual.ifPresent(product -> assertEquals(expected, actual.get()));

        verify(productRepository, times(1)).findByName(validName);
    }

    //---------------------------------- save Product ----------------------------------------

    @Test
    public void test_saveProduct() {

        Product product = new Product();
        product.setProductID(1L);
        product.setName("Emre");

        productService.saveProduct(product);

        verify(productRepository, times(1)).save(product);
    }

    //---------------------------------- update product ----------------------------------------

    @Test
    public void test_updateProduct() {

        Product product = new Product();
        product.setProductID(1L);
        product.setName("Emre");

        productService.updateProduct(product);

        verify(productRepository, times(1)).save(product);
    }

    //---------------------------------- Delete product By ID ----------------------------------------

    @Test
    public void test_deleteProductByID_validID() {

        Long validID = 1L;

        productService.deleteProductByID(validID);

        verify(productRepository, times(1)).deleteById(validID);

    }

    @Test
    public void test_deleteProductByID_validIdIsNull_returnsException() {

        Exception exception = assertThrows(NullPointerException.class, () -> productService.deleteProductByID(null));

        assertEquals(NullPointerException.class, exception.getClass());

    }

    //---------------------------------- Delete All Product ----------------------------------------
    @Test
    public void test_deleteAllProduct() {

        productService.deleteAllProducts();

        verify(productRepository, times(1)).deleteAll();

    }

}
