package com.shop.config;

import com.shop.product.model.Product;
import com.shop.product.repository.ProductRepository;
import com.shop.user.model.User;
import com.shop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @SuppressWarnings("null")
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            productRepository.saveAll(List.of(
                Product.builder().name("MacBook Pro 16").description("Space Gray, 1TB SSD").price(new BigDecimal("2499.00")).stockQuantity(10).build(),
                Product.builder().name("iPhone 15 Pro").description("Titanium Black, 256GB").price(new BigDecimal("1099.00")).stockQuantity(25).build(),
                Product.builder().name("AirPods Max").description("Sky Blue, Noise-Cancelling").price(new BigDecimal("549.00")).stockQuantity(15).build(),
                Product.builder().name("Apple Watch Ultra 2").description("Titanium Case, Orange Loop").price(new BigDecimal("799.00")).stockQuantity(12).build(),
                Product.builder().name("iPad Pro 12.9").description("M2 Chip, 512GB").price(new BigDecimal("1399.00")).stockQuantity(8).build(),
                Product.builder().name("Magic Keyboard").description("Backlit, Multi-Touch Trackpad").price(new BigDecimal("349.00")).stockQuantity(20).build()
            ));
        }

        if (userRepository.count() == 0) {
            userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .build());
        }
    }
}
