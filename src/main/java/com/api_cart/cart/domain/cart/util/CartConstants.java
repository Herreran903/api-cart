package com.api_cart.cart.domain.cart.util;

public class CartConstants {
    private CartConstants() {
        throw new AssertionError();
    }

    public static final int MIN_QUANTITY_VALUE = 0;

    public static final String CART_PRODUCT = "product";
    public static final String CART_QUANTITY = "quantity";

    public static final String CART_TABLE_NAME = "cart";
    public static final String CART_PRODUCT_TABLE_NAME = "cart_product";
}
