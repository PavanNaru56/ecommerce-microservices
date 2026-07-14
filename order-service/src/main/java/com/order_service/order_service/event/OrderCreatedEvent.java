package com.order_service.order_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreatedEvent {

    private Long orderId;
    private Long productId;
    private String username;
    private Integer quantity;
    private BigDecimal totalPrice;



}
