package com.api_cart.cart.domain.cart.spi;

import com.api_cart.cart.domain.cart.model.Cart;

import java.util.Optional;

public interface ICartPersistencePort {
    Cart createCart(Cart cart);
    void addProductToCart(Cart cart);
    Optional<Cart> getCartByUserId(Long userId);
}
