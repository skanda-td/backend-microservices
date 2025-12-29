package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repo;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderController(OrderRepository repo, KafkaTemplate<String, String> kafkaTemplate) {
        this.repo = repo;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        Order saved = repo.save(order);
        kafkaTemplate.send("order.created", saved.getId().toString());
        return saved;
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin access";
    }
}
