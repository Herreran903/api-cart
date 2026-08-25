package com.api_cart.cart.domain.cart.spi;

import java.time.LocalDate;

public interface IFeignTransactionAdapterPort {
    LocalDate getRestockDate(Long productId);
}
