package com.api_cart.cart.domain.cart.spi;

public interface IJwtAdapterPort {
    Long getUserId(String token);
}
