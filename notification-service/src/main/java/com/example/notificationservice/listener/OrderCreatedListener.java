package com.example.notificationservice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);

    @KafkaListener(topics = "order.created", groupId = "notification-group")
    public void consume(String message) {
        log.info("Received order created message: {}", message);
    }
}
