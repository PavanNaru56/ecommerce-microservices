package com.order_service.order_service.service;

import com.order_service.order_service.clients.ProductClient;
import com.order_service.order_service.dtos.OrderRequest;
import com.order_service.order_service.dtos.OrderResponse;
import com.order_service.order_service.dtos.ProductResponse;
import com.order_service.order_service.event.OrderCreatedEvent;
import com.order_service.order_service.exception.ProductServiceUnavailableException;
import com.order_service.order_service.model.Order;
import com.order_service.order_service.model.OrderStatus;
import com.order_service.order_service.producer.OrderEventProducer;
import com.order_service.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderEventProducer orderEventProducer;

//    OrderService(OrderRepository orderRepository, ProductClient productClient, OrderEventProducer orderEventProducer) {
//        this.orderRepository = orderRepository;
//        this.productClient = productClient;
//        this.orderEventProducer = orderEventProducer;
//
//    }



    @CircuitBreaker(
            name = "productServiceCB",
            fallbackMethod = "createOrderFallback"
    )
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

        // create the event

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(order1.getId())
                .productId(order1.getProductId())
                .username(order1.getUsername())
                .quantity(order1.getQuantity())
                .totalPrice(totalPrice)
                .build();

        orderEventProducer.publishOrderCreatedEvent(event);

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

    public OrderResponse createOrderFallback(OrderRequest orderRequest, String username, Exception e) {

        System.out.println("Fallback executed: " + e.getMessage());

        throw new ProductServiceUnavailableException("Product Service Unavailable. Please try again later");


    }



}
