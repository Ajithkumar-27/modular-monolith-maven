package com.shop.order.controller;

import com.shop.order.model.Order;
import com.shop.order.model.OrderItem;
import com.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody List<OrderItem> items) {
        return ResponseEntity.ok(orderService.placeOrder(items));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
