package com.foodfast.payment_service.controller;

import org.springframework.web.bind.annotation.*;
import com.foodfast.payment_service.model.Momo.CreateMomoResponse;
import com.foodfast.payment_service.service.MomoService;

@RestController
@RequestMapping("/api/momo")
public class MomoController {

    private final MomoService momoService;

    public MomoController(MomoService momoService) {
        this.momoService = momoService;
    }

    @PostMapping("/create")
    public CreateMomoResponse createPayment() {
        return momoService.createMomoQR();
    }
}
