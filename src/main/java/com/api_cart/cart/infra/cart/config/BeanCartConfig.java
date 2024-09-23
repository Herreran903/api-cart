package com.api_cart.cart.infra.cart.config;

import com.api_cart.cart.domain.cart.api.ICartServicePort;
import com.api_cart.cart.domain.cart.spi.ICartPersistencePort;
import com.api_cart.cart.domain.cart.spi.IFeignStockAdapterPort;
import com.api_cart.cart.domain.cart.spi.IFeignTransactionAdapterPort;
import com.api_cart.cart.domain.cart.spi.IJwtAdapterPort;
import com.api_cart.cart.domain.cart.usecase.CartUseCase;
import com.api_cart.cart.infra.cart.out.CartAdapter;
import com.api_cart.cart.infra.cart.out.ICartMapper;
import com.api_cart.cart.infra.cart.out.ICartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanCartConfig {
    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final IJwtAdapterPort jwtAdapterPort;
    private final IFeignStockAdapterPort feignAdapterPort;
    private final IFeignTransactionAdapterPort feignTransactionAdapterPort;

    @Bean
    public ICartPersistencePort cartPersistence() {
        return new CartAdapter(cartRepository, cartMapper);
    }

    @Bean
    public ICartServicePort cartService() {
        return new CartUseCase(cartPersistence(), jwtAdapterPort, feignAdapterPort, feignTransactionAdapterPort);
    }
}
