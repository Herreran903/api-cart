package com.api_cart.cart.domain.util;

public final class GlobalConstants {
    private GlobalConstants() {
        throw new AssertionError();
    }

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final int TOKEN_SUBSTRING = 7;

    public static final String ROLES = "roles";
    public static final String USER_ID = "id";
}
