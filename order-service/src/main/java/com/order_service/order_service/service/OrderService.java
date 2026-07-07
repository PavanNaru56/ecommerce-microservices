package com.order_service.order_service.service;

import com.order_service.order_service.clients.ProductClient;
import com.order_service.order_service.dtos.OrderRequest;
import com.order_service.order_service.dtos.OrderResponse;
import com.order_service.order_service.dtos.ProductResponse;
import com.order_service.order_service.model.Order;
import com.order_service.order_service.model.OrderStatus;
import com.order_service.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class OrderService {

    private OrderRepository orderRepository;
    private ProductClient productClient;

    OrderService(OrderRepository orderRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    public OrderResponse createOrder(OrderRequest orderRequest, String username) {



        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .username(username)
                .orderStatus(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
        Order order1 = orderRepository.save(order);

        ProductResponse product = productClient.getProductById(orderRequest.getProductId());

        BigDecimal totalPrice = product.getPrice()
                .multiply(
                        BigDecimal.valueOf(orderRequest.getQuantity())

                );


        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(order1.getId());
        orderResponse.setProductId(order1.getProductId());
        orderResponse.setUsername(order1.getUsername());
        orderResponse.setQuantity(order1.getQuantity());
        orderResponse.setTotalPrice(totalPrice);
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
