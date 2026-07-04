package com.order_service.order_service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class OrderRequest {

    @NotNull
    private Long productId;

    @NotNull
    @Positive
    private Integer quantity;


}
