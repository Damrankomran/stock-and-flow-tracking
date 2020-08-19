package com.damrankomran.poc_product_api.controller;

import com.damrankomran.poc_product_api.model.Product;
import com.damrankomran.poc_product_api.service.ProductService;
import com.damrankomran.poc_product_api.util.CustomErrorType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final ProductService productService;

    //------------------------ Get All Product ------------------------------

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ResponseEntity<?> getAllProduct() {
        log.info("Get All Product");
        try {
            List<Product> products = productService.findAllProduct();

            if (products.isEmpty()) {
                log.info("Products not found!");
                return new ResponseEntity<>(new CustomErrorType("Products not found!"), HttpStatus.NOT_FOUND);
            }

            log.info("Products found successful!");
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new CustomErrorType(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    //------------------------ Get Product ------------------------------
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id) {
        log.info("getProduct");
        try {
            validateProductID(id);
            Optional<Product> product = productService.findByID(id);

            if (!product.isPresent()) {
                log.error("Products id {} not found.", id);
                return new ResponseEntity<>(new CustomErrorType("Products id " + id + " not found!"),
                        HttpStatus.NOT_FOUND);
            }

            log.info("Products found! {}", product.get());
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    //------------------------ Save Product ------------------------------
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ResponseEntity<?> saveProduct(@RequestBody Product product, UriComponentsBuilder ucBuilder) {
        log.info("Create Product -> "+product);
        try {
            validateRequestBodySaveProduct(product);

            if (productService.isProductExist(product)) {
                log.error("Unable to create. A Product with name {} already exist", product.getName());
                return new ResponseEntity<>(new CustomErrorType("Unable to create. A Product with name " +
                        product.getName() + " already exist."), HttpStatus.CONFLICT);
            }
            product.setUpdateDate(new Date());
            productService.saveProduct(product);
            log.info("Create product successful {}", product);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/api/product/{id}").buildAndExpand(product.getProductID()).toUri());
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //------------------------ Update Product ------------------------------
    @RequestMapping(value = "/product/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        log.info("Updating Product");
        log.info("Product Searching");
        try{
            validateProductID(id);
            validateRequestBodySaveProduct(product);

            Optional<Product> currentProduct = productService.findByID(id);

            if (!currentProduct.isPresent()) {
                log.error("Unable to update. product with id {} not found.", id);
                return new ResponseEntity<>(new CustomErrorType("Unable to update. A product with id " +
                        product.getProductID() + " not found."), HttpStatus.NOT_FOUND);
            }
            //Yeni gelen product objesi id'leri eşleşen currentProduct nesnesinde productName değişikliğini kontrol ediyoruz.
            //Eğer productName değiştirilmiş ve yeni productName db'de başka bir productName ile eşleşiyorsa, çakışma olacağından dolayı
            //isExist hatası döndürüyoruz.
            if(!currentProduct.get().getName().equals(product.getName())){
                log.info("Product name changed");
                if (productService.isProductExist(product)) {
                    log.error("Unable to create. A product with name {} already exist", product.getName());
                    return new ResponseEntity<>(new CustomErrorType("Unable to create. A product with name " +
                            product.getName() + " already exist."), HttpStatus.CONFLICT);
                }
            }

            //updateDate'i güncelenen tarih olarak sistem tarihini yazdırıyoruz.
            product.setUpdateDate(new Date());
            product.setProductID(currentProduct.get().getProductID());

            log.info("Current product : {}", currentProduct.get());
            productService.updateProduct(product);
            log.info("Update product : {}", product);

            return new ResponseEntity<>(product, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //------------------------ Delete a Product ------------------------------
    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        log.info("Fetching & Deleting Product with id {}", id);

        try {
            validateProductID(id);
            Optional<Product> product = productService.findByID(id);

            if (!product.isPresent()) {
                log.error("Unable to delete. product with id {} not found.", id);
                return new ResponseEntity<>(new CustomErrorType("Unable to delete. product with id " + id + " not found."),
                        HttpStatus.NOT_FOUND);
            }

            log.info("Product with id {} found successful!",product.get().getProductID());
            productService.deleteProductByID(product.get().getProductID());
            log.info("Delete product with id {} successful! product {}",product.get().getProductID(), product.get());

            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //------------------------ Delete All Products ------------------------------
    @RequestMapping(value = "/product", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAllProducts() {
        log.info("Deleting All product");
        try {
            productService.deleteAllProducts();
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    // ------------------- Validate ProductID -----------------------------
    private void validateProductID(@NonNull Long productID) {
        log.info("validateProductID");
        if(productID == null){
            throw new RuntimeException("productID can not be null!");
        }
        if (Long.valueOf(0L).equals(productID)) {
            throw new RuntimeException("productID can not be 0!");
        }
        if(productID < 0){
            throw new RuntimeException("productID can not be negative value!");
        }
    }

    private void validateRequestBodySaveProduct(Product product){
        log.info("validateRequestBodySaveProduct");
        Optional<Long> optionalProductID = Optional.ofNullable(product.getProductID());
        Optional<Date> optionalProductUpdateDate = Optional.ofNullable(product.getUpdateDate());

        if (optionalProductID.isPresent()) {
            log.info("productID is generated value. It cannot be sent within the Request Body.");
            throw new RuntimeException("productID is generated value. It cannot be sent within the Request Body.");
        }
        if (optionalProductUpdateDate.isPresent()) {
            log.info("updateDate is generated value. It cannot be sent within the Request Body.");
            throw new RuntimeException("updateDate is generated value. It cannot be sent within the Request Body.");
        }
    }
}
