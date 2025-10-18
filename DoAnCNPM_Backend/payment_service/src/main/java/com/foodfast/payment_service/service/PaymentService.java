package com.foodfast.payment_service.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.foodfast.payment_service.model.Payment;
import com.foodfast.payment_service.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updatePaymentStatus(Long id, Integer status) {
        return paymentRepository.findById(id)
                .map(p -> {
                    p.setStatus(status);
                    return paymentRepository.save(p);
                })
                .orElse(null);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}
