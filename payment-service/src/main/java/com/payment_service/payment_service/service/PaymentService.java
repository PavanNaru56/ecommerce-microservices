package com.payment_service.payment_service.service;


import com.payment_service.payment_service.dtos.PaymentRequest;
import com.payment_service.payment_service.dtos.PaymentResponse;
import com.payment_service.payment_service.event.OrderCreatedEvent;
import com.payment_service.payment_service.event.PaymentProcessedEvent;
import com.payment_service.payment_service.exception.PaymentAlreadyProcessedException;
import com.payment_service.payment_service.exception.PaymentNotFoundException;
import com.payment_service.payment_service.model.Payment;
import com.payment_service.payment_service.model.PaymentStatus;
import com.payment_service.payment_service.producer.PaymentEventProducer;
import com.payment_service.payment_service.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    public PaymentResponse createPayment(PaymentRequest paymentRequest) {

        Payment payment = Payment.builder()
                .orderId(paymentRequest.getOrderId())
                .amount(paymentRequest.getAmount())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        Payment savedPayment =  paymentRepository.save(payment);

        return mapToPaymentResponse(savedPayment);


    }

    public List<PaymentResponse> getAllPayments(){

        List<Payment> payments = paymentRepository.findAll();

        List<PaymentResponse> paymentResponseList = payments.stream().map(this::mapToPaymentResponse).toList();

        return paymentResponseList;
    }

    public PaymentResponse getPaymentById(Long id){

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(("Payment not found with id " + id)));

        return mapToPaymentResponse(payment);
    }

    public void savePaymentAuto(OrderCreatedEvent orderCreatedEvent){

        Payment payment = Payment.builder().
                orderId(orderCreatedEvent.getOrderId())
                .amount(orderCreatedEvent.getTotalPrice())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);
    }

    @Transactional
    public PaymentResponse processPayment(Long paymentId){

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment  not found with id " + paymentId));
        
        if(payment.getStatus() == PaymentStatus.SUCCESS){

            throw new PaymentAlreadyProcessedException("Payment already processed");
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        PaymentProcessedEvent paymentProcessedEvent = PaymentProcessedEvent.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .status(String.valueOf(payment.getStatus()))
                .createdAt(payment.getCreatedAt())
                .build();

        paymentEventProducer.publish(paymentProcessedEvent);







        return mapToPaymentResponse(payment);




    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .amount((payment.getAmount()))
                .status(payment.getStatus().toString())
                .createdAt(payment.getCreatedAt())
                .build();


    }

}
