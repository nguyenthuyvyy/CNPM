package com.foodfast.payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.foodfast.payment_service.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrderId(String orderId);
}
