package com.foodfast.payment_service.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.foodfast.payment_service.model.Payment;
import com.foodfast.payment_service.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{orderId}")
    public Payment getPaymentByOrderId(@PathVariable String orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }

    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

    @PutMapping("/{id}/status")
    public Payment updatePaymentStatus(@PathVariable Long id, @RequestParam Integer status) {
        return paymentService.updatePaymentStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }
}
