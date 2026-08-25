package com.api_cart.cart.domain.cart.exception.ex;

public class CategoryLimitExceededException extends RuntimeException {
    public CategoryLimitExceededException(String message) {
        super(message);
    }
}
