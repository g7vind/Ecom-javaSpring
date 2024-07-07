package com.example.jscom;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public void addToCart(String productId, String userId) {
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setPurchased(false);
        cartItem.setUserId(userId);
        cartItemRepository.save(cartItem);
    }

    public void markPurchased(String userId) {
        cartItemRepository.markPurchased(userId);
    }
}
