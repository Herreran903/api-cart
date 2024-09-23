package com.api_cart.cart.infra.feign.stock;

import com.api_cart.cart.app.cart.dto.CategoryIdListResponse;
import com.api_cart.cart.app.cart.dto.ProductIdListRequest;
import com.api_cart.cart.app.cart.dto.ProductIdRequest;
import com.api_cart.cart.infra.feign.FeignClientInterceptor;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${stock.service.name}", url = "${stock.service.url}", configuration = FeignClientInterceptor.class)
public interface StockFeignClient {
    @PostMapping("${stock.service.url.categories}")
    CategoryIdListResponse getListCategoriesOfProducts(@Valid @RequestBody ProductIdListRequest productIdListRequest);

    @PostMapping("${stock.service.url.stock}")
    Integer getStockOfProduct(@Valid @RequestBody ProductIdRequest productIdRequest);
}
