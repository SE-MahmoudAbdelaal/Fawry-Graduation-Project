package com.e_commerce.order_api.service;

import com.e_commerce.order_api.model.TransactionRequest;
import org.springframework.http.ResponseEntity;

public interface BankService {
    ResponseEntity<String> deposit(TransactionRequest transactionRequest);
    ResponseEntity<String> withdraw(TransactionRequest transactionRequest);
}
