package com.damrankomran.poc_product_api;

import com.damrankomran.poc_product_api.controller.ApiController;
import com.damrankomran.poc_product_api.model.Product;
import com.damrankomran.poc_product_api.repo.ProductRepository;
import com.damrankomran.poc_product_api.service.ProductService;
import com.damrankomran.poc_product_api.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiControllerTest {

    @Mock
    private ProductRepository productRepository;

    ApiController apiController;

    @BeforeEach
    public void init(){
        ProductService productService = new ProductServiceImpl(productRepository);
        apiController = new ApiController(productService);
    }

    //------------------------ Get All Product ------------------------------
    @Test
    public void test_getAllProduct_returnsProducts(){

        Product product1 = new Product();
        product1.setProductID(1L);
        product1.setName("product1");

        Product product2 = new Product();
        product2.setProductID(2L);
        product2.setName("product1");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        ResponseEntity<?> responseEntity = apiController.getAllProduct();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void test_getAllProduct_returnsNotFound(){

        ResponseEntity<?> responseEntity = apiController.getAllProduct();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    //------------------------ Save Product ------------------------------
    @Test
    public void test_saveProduct(){

        Product product = new Product();
        product.setName("product 1");
        product.setPrice(100);
        product.setCount(20);

        ResponseEntity<?> responseEntity = apiController.saveProduct(new Product(), UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
    }

    @Test
    public void test_saveProduct_validIdIsNotNull(){

        Long validID = 1L;
        Product product = new Product();
        product.setProductID(validID);

        ResponseEntity<?> responseEntity = apiController.saveProduct(product, UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    @Test
    public void test_saveProduct_isExist(){

        Product product = new Product();
        product.setName("product 1");

        when(productRepository.findByName(product.getName())).thenReturn(product);

        ResponseEntity<?> responseEntity = apiController.saveProduct(product, UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.CONFLICT,responseEntity.getStatusCode());
    }

    @Test
    public void test_saveProduct_updateDateNotNull(){

        Product product = new Product();
        product.setName("product 1");
        product.setUpdateDate(new Date());

        ResponseEntity<?> responseEntity = apiController.saveProduct(product, UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }


    //------------------------ Update Product ------------------------------
    @Test
    public void test_updateProduct_validID(){

        Long validID = 1L;

        Product product = new Product();
        product.setName("product 1");
        product.setPrice(100);
        product.setCount(20);

        Product currentProduct = new Product();
        currentProduct.setProductID(validID);
        currentProduct.setName("Product 1");
        currentProduct.setPrice(10);
        currentProduct.setCount(0);

        when(productRepository.findById(validID)).thenReturn(java.util.Optional.of(currentProduct));

        ResponseEntity<?> responseEntity = apiController.updateProduct(validID,product);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        verify(productRepository,times(1)).save(product);
    }

    @Test
    public void test_updateProduct_validIdIsNull(){

        ResponseEntity<?> responseEntity = apiController.updateProduct(null,new Product());

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    @Test
    public void test_updateProduct_validIdIsZero(){

        ResponseEntity<?> responseEntity = apiController.updateProduct(0L,new Product());

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    @Test
    public void test_updateProduct_validIdIsNegativeValue(){

        ResponseEntity<?> responseEntity = apiController.updateProduct(-1L,new Product());

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    @Test
    public void test_updateProduct_NotFound(){
        ResponseEntity<?> responseEntity = apiController.updateProduct(1L,new Product());

        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    @Test
    public void test_updateProduct_validDateIsNotNull(){

        Product product = new Product();
        product.setUpdateDate(new Date());

        ResponseEntity<?> responseEntity = apiController.updateProduct(1L,product);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    @Test
    public void test_updateProduct_isExist(){

        Product product = new Product();
        product.setProductID(1L);
        product.setName("Product 1");

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setCount(10);

        when(productRepository.findById(product.getProductID())).thenReturn(java.util.Optional.of(product));
        when(productRepository.findByName(product2.getName())).thenReturn(new Product());

        ResponseEntity<?> responseEntity = apiController.updateProduct(1L,product2);

        assertEquals(HttpStatus.CONFLICT,responseEntity.getStatusCode());
    }

    //------------------------ Delete Product By ID ------------------------------
    @Test
    public void test_deleteProductByID_validID(){

        Long validID = 1L;
        Product product = new Product();
        product.setProductID(1L);

        when(productRepository.findById(validID)).thenReturn(java.util.Optional.of(product));
        ResponseEntity<?> responseEntity = apiController.deleteProduct(validID);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void test_deleteProductByID_validID_notFound(){

        Long validID = 1L;

        ResponseEntity<?> responseEntity = apiController.deleteProduct(validID);

        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    @Test
    public void test_deleteProductByID_validIdIsNotNull(){

        ResponseEntity<?> responseEntity = apiController.deleteProduct(null);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    @Test
    public void test_deleteProductByID_validIdIsZero(){

        ResponseEntity<?> responseEntity = apiController.deleteProduct(0L);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    @Test
    public void test_deleteProductByID_validIdIsNegativeValue(){

        ResponseEntity<?> responseEntity = apiController.deleteProduct(-1L);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    //------------------------ Delete All Products ------------------------------
    @Test
    public void test_deleteAllProduct(){

        ResponseEntity<?> responseEntity = apiController.deleteAllProducts();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }
}
