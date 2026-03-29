package com.shop.payment.service;

import com.shop.order.service.OrderService;
import com.shop.payment.model.Payment;
import com.shop.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    @Transactional
    @SuppressWarnings("null")
    public Payment processPayment(Long orderId, BigDecimal amount) {
        // Simple simulation: All payments are successful
        Payment payment = Payment.builder()
                .orderId(orderId)
                .paymentDate(LocalDateTime.now())
                .amount(amount)
                .status("SUCCESS")
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Update order status after successful payment
        orderService.updateOrderStatus(orderId, "PAID");

        return savedPayment;
    }

    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));
    }
}
