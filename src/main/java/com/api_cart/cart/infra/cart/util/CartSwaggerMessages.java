package com.api_cart.cart.infra.cart.util;

public class CartSwaggerMessages {
    private CartSwaggerMessages(){
        throw new AssertionError();
    }

    public static final String INCREASE_CART_REQUEST_EXAMPLE =
            "{ \"product\": \"1\", " +
            "\"quantity\": \"2\"}";
}