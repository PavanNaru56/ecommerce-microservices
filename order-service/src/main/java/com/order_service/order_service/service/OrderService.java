package com.order_service.order_service.service;

import com.order_service.order_service.dtos.OrderRequest;
import com.order_service.order_service.dtos.OrderResponse;
import com.order_service.order_service.model.Order;
import com.order_service.order_service.model.OrderStatus;
import com.order_service.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {

        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .username("pavannaru")
                .orderStatus(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
        Order order1 = orderRepository.save(order);

        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(order1.getId());
        orderResponse.setProductId(order1.getProductId());
        orderResponse.setUsername(order1.getUsername());
        orderResponse.setQuantity(order1.getQuantity());
        orderResponse.setTotalPrice(order1.getTotalPrice());
        orderResponse.setOrderStatus(order1.getOrderStatus());

        return orderResponse;



    }

    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(order.getId());
        orderResponse.setProductId(order.getProductId());
        orderResponse.setUsername(order.getUsername());
        orderResponse.setQuantity(order.getQuantity());
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setOrderStatus(order.getOrderStatus());
        return orderResponse;


    }

    public List<OrderResponse> getAllOrders() {

        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponseList = orders.stream()
                .map(this :: mapperToOrderResponse)
                .collect(Collectors.toList());
        return orderResponseList;




    }

    public OrderResponse mapperToOrderResponse(Order order) {

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setProductId(order.getProductId());
        orderResponse.setUsername(order.getUsername());
        orderResponse.setQuantity(order.getQuantity());
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setOrderStatus(order.getOrderStatus());
        return orderResponse;

    }
}
