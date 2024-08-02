package com.e_commerce.order_api.service.impl;

import com.e_commerce.order_api.entity.Item;
import com.e_commerce.order_api.entity.Product;
import com.e_commerce.order_api.feginClient.ProductClient;
import com.e_commerce.order_api.feginClient.StockClient;
import com.e_commerce.order_api.redisClient.CartRedisRepository;
import com.e_commerce.order_api.service.CartService;
import com.e_commerce.order_api.utilitie.CartUtilities;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CartServiceImpl implements CartService {
    private ProductClient productClient;
    private StockClient stockClient;
    @Autowired
    private CartRedisRepository cartRedisRepository;

    @Override
    public void addItemToCart(String cartId, Long productId, Integer quantity) {
        Product product = productClient.getProductById(productId);
//        if(stockClient.checkAvailability(productId)) {
            Item item = new Item(quantity, product, CartUtilities.getSubTotalForItem(product, quantity));
            cartRedisRepository.addItemToCart(cartId, item);
//            stockClient.consumeStock(productId);
//        }
    }

    @Override
    public List<Object> getCart(String cartId) {
        return (List<Object>) cartRedisRepository.getCart(cartId, Item.class);
    }

    @Override
    public void changeItemQuantity(String cartId, Long productId, Integer quantity) {
            List<Item> cart = (List) cartRedisRepository.getCart(cartId, Item.class);
            for(Item item :cart){
                if((item.getProduct().getId()).equals(productId)){
//                    if(stockClient.checkAvailability(productId)){
                    cartRedisRepository.deleteItemFromCart(cartId,item);
                    item.setQuantity(quantity);
                    item.setSubTotal(CartUtilities.getSubTotalForItem(item.getProduct(),quantity));
                    cartRedisRepository.addItemToCart(cartId,item);
  //            stockClient.consumeStock(productId);
//                }
                }
            }
    }

    @Override
    public void deleteItemFromCart(String cartId, Long productId) {
        List<Item> cart = (List) cartRedisRepository.getCart(cartId, Item.class);
        for(Item item :cart){
            if((item.getProduct().getId()).equals(productId)){
                cartRedisRepository.deleteItemFromCart(cartId,item);
//                stockClient.addToStock(productId);
            }
        }
    }

    @Override
    public boolean checkIfItemIsExist(String cartId, Long productId) {
        List<Item> cart = (List) cartRedisRepository.getCart(cartId, Item.class);
        for(Item item :cart){
            if((item.getProduct().getId()).equals(productId)){
               return true;
   }
        }
        return false;
    }

    @Override
    public List<Item> getAllItemsFromCart(String cartId) {
        List<Item> items = (List) cartRedisRepository.getCart(cartId, Item.class);
        return items;
    }

    @Override
    public void deleteCart(String cartId) {
        cartRedisRepository.deleteCart(cartId);
    }
}
