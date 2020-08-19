package com.damrankomran.poc_product_api.service;

import com.damrankomran.poc_product_api.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> findByID(Long productID);

    Product findByName(String name);

    void saveProduct(Product product);

    void updateProduct(Product product);

    void deleteProductByID(Long productID);

    void deleteAllProducts();

    List<Product> findAllProduct();

    boolean isProductExist(Product product);
}
