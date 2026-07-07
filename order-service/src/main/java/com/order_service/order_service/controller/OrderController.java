package com.order_service.order_service.controller;

import com.order_service.order_service.dtos.OrderRequest;
import com.order_service.order_service.dtos.OrderResponse;
import com.order_service.order_service.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest orderRequest, @RequestHeader("X-User-Username") String username) {

        return ResponseEntity.ok(orderService.createOrder(orderRequest, username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id){

        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderResponse>> findAll(){

        return ResponseEntity.ok(orderService.getAllOrders());
    }

//    @GetMapping("/test-user")
//    public String getUsername(@RequestHeader("X-User-Username") String username, @RequestHeader("X-User-Role") String role){
//
//        return username + " " + role;
//    }
}
