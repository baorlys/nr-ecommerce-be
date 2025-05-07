package com.nr.ecommercebe.module.cart.web;

import com.nr.ecommercebe.module.cart.application.domain.CartItem;
import com.nr.ecommercebe.module.cart.application.dto.request.CartItemRequestDto;
import com.nr.ecommercebe.module.cart.application.service.CartService;
import com.nr.ecommercebe.module.user.application.service.authentication.AuthService;
import com.nr.ecommercebe.shared.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CartController {
    CartService cartService;
    AuthService authService;

    @PostMapping
    public ResponseEntity<Void> addToCart(HttpServletRequest request, @RequestBody CartItemRequestDto item) {
        String accessToken = CookieUtil.getCookieValue(request, CookieUtil.ACCESS_TOKEN_NAME);
        String userId = authService.getUserIdFromToken(accessToken);
        cartService.addToCart(userId, item);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(HttpServletRequest request) {
        String accessToken = CookieUtil.getCookieValue(request, CookieUtil.ACCESS_TOKEN_NAME);
        String userId = authService.getUserIdFromToken(accessToken);
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeFromCart(HttpServletRequest request, @PathVariable String itemId) {
        String accessToken = CookieUtil.getCookieValue(request, CookieUtil.ACCESS_TOKEN_NAME);
        String userId = authService.getUserIdFromToken(accessToken);
        cartService.removeFromCart(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(HttpServletRequest request) {
        String accessToken = CookieUtil.getCookieValue(request, CookieUtil.ACCESS_TOKEN_NAME);
        String userId = authService.getUserIdFromToken(accessToken);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }




}
