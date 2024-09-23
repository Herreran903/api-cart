package com.api_cart.cart.domain.cart.api;

import com.api_cart.cart.domain.cart.model.CartProduct;

public interface ICartServicePort {
    void addProductToCart(CartProduct cartProduct, String token);
    void deleteArticleOfCart(Long productId, String token);
}
