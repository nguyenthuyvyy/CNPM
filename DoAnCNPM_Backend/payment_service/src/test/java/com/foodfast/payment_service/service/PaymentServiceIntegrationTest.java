package com.foodfast.payment_service.service;

import com.foodfast.payment_service.model.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    @Transactional
    void testCreateAndRetrievePayment() {
        Payment payment = Payment.builder()
                .orderId("ORD123")
                .status(0)
                .amount(BigDecimal.valueOf(100))
                .build();

        Payment saved = paymentService.createPayment(payment);
        assertNotNull(saved.getId());

        Payment retrieved = paymentService.getPaymentByOrderId("ORD123");
        assertNotNull(retrieved);
        assertEquals("ORD123", retrieved.getOrderId());
    }
}
