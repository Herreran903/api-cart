package com.api_cart.cart.infra.security.jwt;

import static com.api_cart.cart.domain.util.GlobalConstants.TOKEN_SUBSTRING;

public class JwtUtility {
    private JwtUtility() {
        throw new AssertionError();
    }

    public static String extractJwt(String authHeader) {
        return authHeader.substring(TOKEN_SUBSTRING);
    }
}
