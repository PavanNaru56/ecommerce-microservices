package com.payment_service.payment_service.controller;

import com.payment_service.payment_service.dtos.PaymentRequest;
import com.payment_service.payment_service.dtos.PaymentResponse;
import com.payment_service.payment_service.repository.PaymentRepository;
import com.payment_service.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest){

        PaymentResponse paymentResponse =  paymentService.createPayment(paymentRequest);

        return ResponseEntity.ok().body(paymentResponse);

    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments(){
        return ResponseEntity.ok().body(paymentService.getAllPayments());
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id){
        return ResponseEntity.ok().body(paymentService.getPaymentById(id));
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<?> processPayment(@PathVariable Long id){
        return ResponseEntity.ok().body(paymentService.processPayment(id));
    }

}
