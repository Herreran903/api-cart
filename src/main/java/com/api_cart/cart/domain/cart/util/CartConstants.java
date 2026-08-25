package com.api_cart.cart.domain.cart.util;

public class CartConstants {
    private CartConstants() {
        throw new AssertionError();
    }

    public static final int MIN_QUANTITY_VALUE = 0;
    public static final int MAX_CATEGORIES_VALUE = 3;

    public static final String CART_PRODUCT = "product";
    public static final String CART_QUANTITY = "quantity";
    public static final String CART = "cart";

    public static final String PRODUCTS = "products";
    public static final String PRODUCT = "product";
    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_BRAND = "brand";
    public static final String PRODUCT_CATEGORY = "category";
    public static final String PRODUCT_NAME = "name";

    public static final String RESTOCK_DATE = "restockDate";

    public static final String CART_TABLE_NAME = "cart";
    public static final String CART_PRODUCT_TABLE_NAME = "cart_product";

}
