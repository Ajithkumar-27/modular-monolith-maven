package com.shop.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendEmail(String to, String subject, String body) {
        log.info("Sending Email to: {} | Subject: {} | Body: {}", to, subject, body);
    }

    public void sendSms(String phoneNumber, String message) {
        log.info("Sending SMS to: {} | Message: {}", phoneNumber, message);
    }

    public void sendOrderConfirmation(String email, Long orderId) {
        sendEmail(email, "Order Confirmation", "Your order #" + orderId + " has been placed successfully.");
    }
}
