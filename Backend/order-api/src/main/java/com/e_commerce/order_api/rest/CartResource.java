package com.e_commerce.order_api.rest;

import com.e_commerce.order_api.header.HeaderGenerator;
import com.e_commerce.order_api.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cart")
public class CartResource {
    @Autowired
    CartService cartService;
    HeaderGenerator headerGenerator;

    @GetMapping
    public ResponseEntity<List<Object>> getCart(@RequestHeader(value = "Cookie") String cartId){
        List<Object>cart = cartService.getCart(cartId);
        if(!cart.isEmpty()){
            return new ResponseEntity<List<Object>>(
                    cart,
                    HttpStatus.OK);
        }
        return new ResponseEntity<List<Object>>(
                HttpStatus.NOT_FOUND);
    }
    @PostMapping(params = {"productId","quantity"})
    public ResponseEntity<List<Object>> addItemToCart(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") Integer quantity,
            @RequestHeader(value = "Cookie") String cardId,
            HttpServletRequest request
            ) {
        List<Object> cart = cartService.getCart(cardId);
        if (cart != null) {
            if (cart.isEmpty()) {
                cartService.addItemToCart(cardId, productId, quantity);
            } else {
                if (cartService.checkIfItemIsExist(cardId, productId)) {
                    cartService.changeItemQuantity(cardId, productId, quantity);
                } else {
                    cartService.addItemToCart(cardId, productId, quantity);
                }
            }
            return new ResponseEntity<List<Object>>(cart,
//                    headerGenerator.getHeadersForSuccessPostMethod(request, Long.parseLong(cardId)),
                    HttpStatus.CREATED);
        }

        return new ResponseEntity<List<Object>>(
                headerGenerator.getHeadersForError(),
                HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping(params = "productId")
    public ResponseEntity<Void>removeItemFromCart(
            @RequestParam("productId") Long productId,
            @RequestHeader(value = "Cookie") String cartId){
        List<Object>cart = cartService.getCart(cartId);
        if(cart!=null){
            cartService.deleteItemFromCart(cartId,productId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
}
