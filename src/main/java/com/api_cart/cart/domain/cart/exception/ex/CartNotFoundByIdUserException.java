package com.api_cart.cart.domain.cart.exception.ex;

public class CartNotFoundByIdUserException extends RuntimeException {
    public CartNotFoundByIdUserException(String message) {
        super(message);
    }
}
