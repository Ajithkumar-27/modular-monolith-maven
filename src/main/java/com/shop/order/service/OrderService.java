package com.shop.order.service;

import com.shop.notification.service.NotificationService;
import com.shop.order.model.Order;
import com.shop.order.model.OrderItem;
import com.shop.order.repository.OrderRepository;
import com.shop.product.service.ProductService;
import com.shop.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final NotificationService notificationService;

    @Transactional
    @SuppressWarnings("null")
    public Order placeOrder(List<OrderItem> items) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItem item : items) {
            var product = productService.getProductById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            productService.reduceStock(product.getId(), item.getQuantity());
            item.setPrice(product.getPrice());
            totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }

        Order order = Order.builder()
                .userId(user.getId())
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .totalAmount(totalAmount)
                .items(items)
                .build();

        Order savedOrder = orderRepository.save(order);

        notificationService.sendOrderConfirmation(user.getUsername(), savedOrder.getId());

        return savedOrder;
    }

    public List<Order> getMyOrders() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderRepository.findByUserId(user.getId());
    }

    @SuppressWarnings("null")
    public void updateOrderStatus(Long orderId, String status) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
        });
    }
}
