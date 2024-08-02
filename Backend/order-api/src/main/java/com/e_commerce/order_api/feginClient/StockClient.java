package com.e_commerce.order_api.feginClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "store-service", url = "http://localhost:8081/")
public interface StockClient {
        @GetMapping(value = "/store/stock/checkAvailability/{productId}")
        public boolean checkAvailability(@PathVariable("productId") Long productId);

        @GetMapping(value = "/store/stock/consume/{productId}")
        public void consumeStock(@PathVariable("productId") Long productId);
        @GetMapping(value = "/store/stock/add/{productId}")
        public void addToStock(@PathVariable("productId") Long productId);

}