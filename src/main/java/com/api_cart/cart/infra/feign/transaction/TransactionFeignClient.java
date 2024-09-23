package com.api_cart.cart.infra.feign.transaction;

import com.api_cart.cart.app.cart.dto.ProductIdRequest;
import com.api_cart.cart.app.cart.dto.RestockDateResponse;
import com.api_cart.cart.infra.feign.FeignClientInterceptor;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${transaction.service.name}", url = "${transaction.service.url}", configuration = FeignClientInterceptor.class)
public interface TransactionFeignClient {
    @PostMapping("${transaction.service.url.restoration}")
    RestockDateResponse getRestocktionDate(@Valid @RequestBody ProductIdRequest productIdRequest);
}
