package com.order_service.order_service.clients;

import com.order_service.order_service.dtos.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product-service",
        url = "http://localhost:8082"
)
public interface ProductClient {

    @GetMapping("/api/products/{Id}")
    ProductResponse getProductById(@PathVariable Long Id);
}
