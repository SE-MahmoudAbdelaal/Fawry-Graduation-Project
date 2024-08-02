package com.e_commerce.order_api.feginClient;

import com.e_commerce.order_api.model.CouponRequest;
import com.e_commerce.order_api.model.CouponResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "coupon-service", url = "http://localhost:8080/api")
public interface CouponClient {
    @GetMapping(value = "/coupons/{code}")
    public CouponResponse getCouponByCode(@PathVariable(value = "code") String couponCode);
    @PostMapping(value = "/coupon-consumptions")
    public void consumeCoupon(@RequestBody CouponRequest couponRequest );
}