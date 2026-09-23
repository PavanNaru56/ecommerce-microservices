package com.payment_service.payment_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentProcessedEvent {


    public Long paymentId;

    public Long orderId;

    public BigDecimal amount;

    public String status;

    public LocalDateTime createdAt;
}
