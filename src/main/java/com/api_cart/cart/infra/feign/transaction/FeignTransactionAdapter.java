package com.api_cart.cart.infra.feign.transaction;

import com.api_cart.cart.app.cart.dto.ProductIdRequest;
import com.api_cart.cart.domain.cart.spi.IFeignTransactionAdapterPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FeignTransactionAdapter implements IFeignTransactionAdapterPort {
    private final TransactionFeignClient transactionFeignClient;

    @Override
    public LocalDate getRestockDate(Long productId) {
        ProductIdRequest productIdRequest = new ProductIdRequest(productId);
        return transactionFeignClient.getRestockDate(productIdRequest).getRestockDate();
    }
}
