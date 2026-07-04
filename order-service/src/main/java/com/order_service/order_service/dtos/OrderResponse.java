package com.order_service.order_service.dtos;

import com.order_service.order_service.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;

    private Long productId;

    private String username;

    private Integer quantity;

    private BigDecimal totalPrice;

    private OrderStatus orderStatus;
}
