package com.api_cart.cart.infra.util;

public class SwaggerMessages {

    private SwaggerMessages() {
        throw new AssertionError();
    }

    public static final String RESPONSE_400_DESCRIPTION = "Invalid input";
    public static final String RESPONSE_200_DESCRIPTION = "Processed successfully";
    public static final String RESPONSE_500_DESCRIPTION = "Internal server error";

    public static final String CODE_200 = "200";
    public static final String CODE_400 = "400";
    public static final String CODE_500 = "500";
}
