package com.foodfast.payment_service.service;

import com.foodfast.payment_service.model.Payment;
import com.foodfast.payment_service.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceUnitTest {

    private PaymentRepository paymentRepository;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentService = new PaymentService(paymentRepository);
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100));

        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentService.createPayment(payment);
        assertNotNull(result);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testUpdatePaymentStatus() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus(0);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment updated = paymentService.updatePaymentStatus(1L, 1);
        assertEquals(0, updated.getStatus());
    }
}
