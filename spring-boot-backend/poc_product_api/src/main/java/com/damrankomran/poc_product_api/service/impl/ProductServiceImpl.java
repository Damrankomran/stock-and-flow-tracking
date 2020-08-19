package com.damrankomran.poc_product_api.service.impl;

import com.damrankomran.poc_product_api.model.Product;
import com.damrankomran.poc_product_api.repo.ProductRepository;
import com.damrankomran.poc_product_api.service.ProductService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Optional<Product> findByID(@NonNull Long productID) {
        return productRepository.findById(productID);
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        saveProduct(product);
    }

    @Override
    public void deleteProductByID(@NonNull Long productID) {
        productRepository.deleteById(productID);
    }

    @Override
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    @Override
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public boolean isProductExist(Product product) {
        return findByName(product.getName()) != null;
    }
}
