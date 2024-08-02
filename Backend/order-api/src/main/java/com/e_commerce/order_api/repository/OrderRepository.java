package com.e_commerce.order_api.repository;

import com.e_commerce.order_api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
