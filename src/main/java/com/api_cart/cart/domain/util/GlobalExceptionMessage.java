package com.api_cart.cart.domain.util;

public final class GlobalExceptionMessage {

    private GlobalExceptionMessage() {
        throw new AssertionError();
    }

    public static final String INVALID_TYPE_PARAM =
            "The parameter '%s' must be of type '%s'";

    public static final String INVALID_JSON =
            "The JSON object is not valid";

    public static final String INVALID_PARAMETERS =
            "The parameters are not valid";

    public static final String INVALID_OBJECT =
            "The object data is not valid";

    public static final String BAD_ROLE =
            "The user's role is invalid or not properly assigned";

    public static final String INVALID_USER =
            "Invalid user provided";

    public static final String INVALID_TOKEN =
            "Invalid JWT Token provided";

    public static final String SERVICE_UNAVAILABLE =
            "Cannot connect to the service";

    public static final String INTERNAL_ERROR =
            "Internal server error in service";

    public static final String INVALID_ORDER =
            "Order must be 'ASC' or 'DESC'";

    public static final String GREATER_ZERO_SIZE =
            "Size must be greater than zero";

    public static final String NO_NEGATIVE_PAGE =
            "Page number must be non-negative";

}
