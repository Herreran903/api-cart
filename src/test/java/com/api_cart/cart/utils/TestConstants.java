package com.api_cart.cart.utils;

import java.time.LocalDate;

public final class TestConstants {

    private TestConstants() {
        throw new AssertionError();
    }

    public static final Long VALID_CART_ID = 1L;
    public static final Long VALID_USER_ID = 1L;
    public static final LocalDate VALID_CREATE_DATE = LocalDate.now();
    public static final LocalDate VALID_UPDATE_DATE = LocalDate.now();
    public static final Long VALID_PRODUCT_ID = 1L;
    public static final Integer VALID_QUANTITY = 1;
    public static final String VALID_TOKEN = "VALID_TOKEN";

}
