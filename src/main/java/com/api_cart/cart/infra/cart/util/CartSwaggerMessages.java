package com.api_cart.cart.infra.cart.util;

public class CartSwaggerMessages {
    private CartSwaggerMessages(){
        throw new AssertionError();
    }

    public static final String ADD_CART_REQUEST_EXAMPLE =
            "{ \"product\": \"1\", " +
            "\"quantity\": \"2\"}";

    public static final String REMOVE_CART_REQUEST_EXAMPLE =
            "{ \"product\": \"1\"}";
}