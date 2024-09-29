package com.api_cart.cart.infra.feign.stock;

import com.api_cart.cart.app.cart.dto.*;
import com.api_cart.cart.domain.page.PageData;
import com.api_cart.cart.infra.feign.FeignClientInterceptor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

import static com.api_cart.cart.domain.util.GlobalConstants.*;
import static com.api_cart.cart.domain.util.GlobalConstants.ASC;
import static com.api_cart.cart.domain.util.GlobalExceptionMessage.*;

@FeignClient(name = "${stock.service.name}", url = "${stock.service.url}", configuration = FeignClientInterceptor.class)
public interface StockFeignClient {
    @PostMapping("${stock.service.url.categories}")
    CategoryIdListResponse getListCategoriesOfProducts(@Valid @RequestBody ProductIdListRequest productIdListRequest);

    @PostMapping("${stock.service.url.stock}")
    Integer getStockOfProduct(@Valid @RequestBody ProductIdRequest productIdRequest);

    @PostMapping("${stock.service.url.prices}")
    Map<Long, BigDecimal> getProductsPrice(@Valid @RequestBody ProductIdListRequest productIdListRequest);

    @PostMapping("${stock.service.url.fetch.cart}")
    PageData<CartProductStockResponse> getProductPage(
            @Min(value = MIN_PAGE_NUMBER, message = NO_NEGATIVE_PAGE)
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false)
            Integer page,
            @Min(value = MIN_PAGE_SIZE, message = GREATER_ZERO_SIZE)
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false)
            Integer size,
            @Pattern(regexp = ORDER_REGEX, message = INVALID_ORDER)
            @RequestParam(defaultValue = ASC, required = false)
            String order,
            @RequestParam(required = false)
            String category,
            @RequestParam(required = false)
            String brand,
            @RequestBody
            ProductIdListRequest productIdListRequest
    );
}
