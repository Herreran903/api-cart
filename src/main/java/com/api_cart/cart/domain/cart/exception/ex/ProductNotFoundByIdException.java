package com.api_cart.cart.domain.cart.exception.ex;

public class ProductNotFoundByIdException extends RuntimeException {
    public ProductNotFoundByIdException(String message) {
        super(message);
    }
}
