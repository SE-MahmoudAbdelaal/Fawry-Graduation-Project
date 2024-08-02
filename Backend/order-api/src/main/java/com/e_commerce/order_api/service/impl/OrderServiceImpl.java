package com.e_commerce.order_api.service.impl;

import com.e_commerce.order_api.entity.Item;
import com.e_commerce.order_api.entity.Order;
import com.e_commerce.order_api.model.TransactionRequest;
import com.e_commerce.order_api.repository.OrderRepository;
import com.e_commerce.order_api.service.BankService;
import com.e_commerce.order_api.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Autowired
    BankService bankService;
    private OrderRepository orderRepo;

    @Override
    public Order saveOrder(Order order ) {
        TransactionRequest transactionRequestCustomer = new TransactionRequest();
        transactionRequestCustomer.setAmount(order.getTotal_amount());
        transactionRequestCustomer.setCardNumber(order.getCard_number());
        transactionRequestCustomer.setCvv(order.getCvv());
        ResponseEntity<String> withdrawResponse = bankService.withdraw(transactionRequestCustomer);
        for (Item item : order.getItems()){
            TransactionRequest transactionRequestMerchant = new TransactionRequest();
            transactionRequestMerchant.setAmount( item.getProduct().getPrice());
            transactionRequestMerchant.setCardNumber( item.getProduct().getMerchantCardNumber());
            transactionRequestMerchant.setCvv( item.getProduct().getMerchantCardCvv());
             bankService.deposit(transactionRequestMerchant);
        }
        if(withdrawResponse.getStatusCode().equals(200)){
            order.setStatus("PAID");
            order.setTransactionId(withdrawResponse.getBody());
            return orderRepo.save(order);
        }
        return null;
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepo.findById(id).orElseThrow(()-> new Exception("Order with ID " + id + " not found"));
    }

    @Override
    public List<Order> findOrderByCustomerId(Long CustomerId) {
        return orderRepo.findByCustomerId(CustomerId);
    }

    @Override
    public List<Order> searchOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepo.findByCreatedAtBetween(startDate, endDate);
    }
}
