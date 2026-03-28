package com.shop.product.service;

import com.shop.product.model.Product;
import com.shop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public boolean reduceStock(Long productId, Integer quantity) {
        return productRepository.findById(productId).map(product -> {
            if (product.getStockQuantity() >= quantity) {
                product.setStockQuantity(product.getStockQuantity() - quantity);
                productRepository.save(product);
                return true;
            }
            return false;
        }).orElse(false);
    }
}
