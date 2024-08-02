package com.e_commerce.order_api.service;

import com.e_commerce.order_api.entity.Order;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
public interface OrderService {
    public Order saveOrder(Order order);

    public List<Order> findAllOrders();

    public Order findOrderById(Long id) throws Exception;

    public List<Order> findOrderByCustomerId(Long id);
    public List<Order> searchOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}