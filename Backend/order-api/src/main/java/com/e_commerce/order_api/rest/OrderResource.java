package com.e_commerce.order_api.rest;


import com.e_commerce.order_api.entity.Item;
import com.e_commerce.order_api.entity.Order;
import com.e_commerce.order_api.entity.User;
import com.e_commerce.order_api.feginClient.CouponClient;
import com.e_commerce.order_api.feginClient.UserClient;
import com.e_commerce.order_api.header.HeaderGenerator;
import com.e_commerce.order_api.model.CouponRequest;
import com.e_commerce.order_api.model.CouponResponse;
import com.e_commerce.order_api.service.BankService;
import com.e_commerce.order_api.service.CartService;
import com.e_commerce.order_api.service.CouponService;
import com.e_commerce.order_api.service.OrderService;
import com.e_commerce.order_api.utilitie.OrderUtilities;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderResource {
    @Autowired
    CouponService couponService;
    @Autowired
    private HeaderGenerator headerGenerator;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserClient userClient;

    @PostMapping(value = "{userId}", params = {"cardNumber", "cardPassword"})
    public ResponseEntity<Order> saveOrder(@PathVariable("userId") Long userId,
                                           @RequestHeader(value = "Cookie") String cartId,
                                           @RequestParam(required = false) String couponCode,
                                           @RequestParam("cardNumber") String cardNumber,
                                           @RequestParam("cvv") String cvv,
                                           HttpServletRequest request) {
        List<Item> cart = cartService.getAllItemsFromCart(cartId);
        User user = userClient.getUserById(userId);
        if (cart != null && user != null && cvv != null && cardNumber != null) {
            Order order = this.createOrder(cart, user, cardNumber, cvv);
            CouponResponse coupon = couponService.couponIsValid(couponCode);
            if (coupon != null) {
                order.setTotal_amount(OrderUtilities.countTotalPriceWithCoupon(cart, coupon.getValue_type(), coupon.getValue()));
            }
            try {
                Order savedOrder = orderService.saveOrder(order);
                if (savedOrder != null) {
                    CouponRequest consumeOrderRequest = new CouponRequest();
                    consumeOrderRequest.setCode(couponCode);
                    consumeOrderRequest.setOrder_id(order.getId());
                    consumeOrderRequest.setCustomer_id(userId);
                    couponService.couponConsumption(consumeOrderRequest);
                    cartService.deleteCart(cartId);
                    return new ResponseEntity<Order>(savedOrder, headerGenerator.getHeadersForSuccessPostMethod(request, order.getId()),
                            HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<Order>(headerGenerator.getHeadersForError(),
                            HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Order>(headerGenerator.getHeadersForError(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<Order>(headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);
    }

    private Order createOrder(List<Item> cart, User user, String cardNumber, String cvv) {
        Order order = new Order();
        order.setItems(cart);
        order.setUser(user);
        order.setCard_number(cardNumber);
        order.setCvv(cvv);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PAYMENT_EXPECTED");
        order.setTotal_amount(OrderUtilities.countTotalPrice(cart));
        return order;
    }

    @GetMapping("/search")
    public List<Order> findOrderByDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return orderService.searchOrdersByDateRange(startDate, endDate);
        }
        return List.of();
    }

    @GetMapping("/search{customerId}")
    public ResponseEntity<List<Order>> findOrderByCustomerName(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.findOrderByCustomerId(customerId));
    }
}