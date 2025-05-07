package com.nr.ecommercebe.module.cart.application.service;

import com.nr.ecommercebe.module.cart.application.domain.Cart;
import com.nr.ecommercebe.module.cart.application.domain.CartItem;
import com.nr.ecommercebe.module.cart.application.dto.request.CartItemRequestDto;
import com.nr.ecommercebe.module.cart.infrastructure.repository.CartRepository;
import com.nr.ecommercebe.module.catalog.application.service.ProductService;
import com.nr.ecommercebe.module.catalog.application.domain.ProductVariant;
import com.nr.ecommercebe.module.user.application.domain.User;
import com.nr.ecommercebe.shared.exception.ErrorCode;
import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    ProductService productService;


    @Override
    public void addToCart(String userId, CartItemRequestDto item) {
        ProductVariant productVariant = productService.getProductVariantById(item.getVariantId());

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(new User(userId));
                    return cartRepository.save(newCart);
                });

        cart.getItems().removeIf(existingItem -> existingItem.getId().equals(item.getVariantId()));
        CartItem cartItem = CartItem.builder()
                .id(item.getVariantId())
                .name(productVariant.getProduct().getName() + " - " + productVariant.getName())
                .variantId(productVariant.getId())
                .quantity(item.getQuantity())
                .price(productVariant.getPrice())
                .build();

        cart.getItems().add(cartItem);

        cartRepository.save(cart);
    }

    @Override
    public List<CartItem> getCart(String userId) {
        return cartRepository.findByUserId(userId)
                .map(Cart::getItems)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.CART_NOT_FOUND.getMessage()));
    }

    @Override
    public void removeFromCart(String userId, String itemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.CART_NOT_FOUND.getMessage()));
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(String userId) {
        cartRepository.findByUserId(userId)
                .ifPresent(cart -> {
                    cart.getItems().clear();
                    cartRepository.delete(cart);
                });

    }
}
