package com.api_cart.cart.domain.cart.exception;

import static com.api_cart.cart.domain.cart.util.CartConstants.MIN_QUANTITY_VALUE;

public class CartExceptionMessage {
    private CartExceptionMessage(){
        throw new AssertionError();
    }

    public static final String EMPTY_PRODUCT =
            "The 'product' field is empty";

    public static final String EMPTY_QUANTITY =
            "The 'quantity' field is empty";

    public static final String MIN_QUANTITY =
            "The 'quantity' field must be greater than " + MIN_QUANTITY_VALUE;

    public static final String OUT_OF_STOCK =
            "The selected product is currently out of stock.";

    public static final String NOT_ENOUGH_STOCK =
            "There is not enough stock available for the selected product";

    public static final String DATE_RESTOCK =
            "The product will be restocked on: ";

    public static final String EXCEEDED_CATEGORIES =
            "Cannot add more than 3 products in category: ";

    public static final String NO_FOUND_PRODUCT =
            "Product does not found";

    public static final String NO_FOUND_CART =
            "Cart does not found";

}
