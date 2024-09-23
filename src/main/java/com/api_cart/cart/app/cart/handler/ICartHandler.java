package com.api_cart.cart.app.cart.handler;

import com.api_cart.cart.app.cart.dto.CartProductRequest;

public interface ICartHandler {
    void addProductToCart(CartProductRequest product, String token);
}
