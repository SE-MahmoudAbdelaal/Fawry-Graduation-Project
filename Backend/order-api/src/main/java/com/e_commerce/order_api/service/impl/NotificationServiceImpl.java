package com.e_commerce.order_api.service.impl;

import com.e_commerce.order_api.config.RabbitMQConfig;
import com.e_commerce.order_api.entity.Order;
import com.e_commerce.order_api.model.OrderNotification;
import com.e_commerce.order_api.service.NotificationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public void sendOrderNotification(Order order) {
        OrderNotification orderNotification = new OrderNotification(order , order.getStatus(),"new order created",LocalDateTime.now());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY,orderNotification);
    }
}
