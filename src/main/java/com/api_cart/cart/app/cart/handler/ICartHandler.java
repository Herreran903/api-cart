package com.api_cart.cart.app.cart.handler;

import com.api_cart.cart.app.cart.dto.CartProductRequest;
import com.api_cart.cart.app.cart.dto.ProductIdRequest;

public interface ICartHandler {
    void addProductToCart(CartProductRequest product, String token);
    void deleteArticleOfCart(ProductIdRequest product, String token);
}
