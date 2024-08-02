package com.e_commerce.order_api.feginClient;

import com.e_commerce.order_api.model.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bank-api", url = "http://localhost:8085/")
public interface BankClient {
    @PostMapping(value = "/bank/transaction/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionRequest transactionRequest);
    @PostMapping(value = "/bank/transaction/withdraw")
    public  ResponseEntity<String> withdraw(@RequestBody TransactionRequest transactionRequest);
}
