package com.e_commerce.order_api.service.impl;

import com.e_commerce.order_api.feginClient.CouponClient;
import com.e_commerce.order_api.model.CouponRequest;
import com.e_commerce.order_api.model.CouponResponse;
import com.e_commerce.order_api.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponClient couponClient;
    @Override
    public CouponResponse couponIsValid(String couponCode) {
        if(couponCode != null){
            CouponResponse coupon =  couponClient.getCouponByCode(couponCode);
            if(coupon != null && coupon.isValid()){
                return coupon;
            }
            }
        return null;
    }

    @Override
    public void couponConsumption(CouponRequest consumeCouponRequest) {
        couponClient.consumeCoupon(consumeCouponRequest);
    }


}
