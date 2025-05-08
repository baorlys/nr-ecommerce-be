package com.nr.ecommercebe.module.cart.application.service;

import com.nr.ecommercebe.module.cart.application.domain.CartItem;
import com.nr.ecommercebe.module.cart.application.dto.request.CartItemRequestDto;

import java.util.List;

public interface CartService {
    void addToCart(String accessToken, CartItemRequestDto item);
    List<CartItem> getCart(String accessToken);
    void removeFromCart(String accessToken, String itemId);
    void clearCart(String accessToken);
}
