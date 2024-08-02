package com.e_commerce.order_api.service.impl;

import com.e_commerce.order_api.feginClient.BankClient;
import com.e_commerce.order_api.model.TransactionRequest;
import com.e_commerce.order_api.service.BankService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankServiceImpl implements BankService {
    BankClient bankClient;
    @Override
    public ResponseEntity<String> deposit(TransactionRequest transactionRequest) {
        return bankClient.deposit(transactionRequest);
    }

    @Override
    public  ResponseEntity<String> withdraw(TransactionRequest transactionRequest) {
        return bankClient.withdraw(transactionRequest);
    }
}
