package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class OrderGraphqlController {

    private final OrderRepository orderRepository;

    public OrderGraphqlController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @QueryMapping
    public List<Order> orders() {
        return orderRepository.findAll();
    }

    @QueryMapping
    public Order order(@Argument UUID id) {
        return orderRepository.findById(id).orElse(null);
    }
}
