package com.api_cart.cart.domain.cart.spi;

import com.api_cart.cart.domain.cart.model.CartProductStock;
import com.api_cart.cart.domain.page.PageData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IFeignStockAdapterPort {
    List<String> getListCategoriesOfProducts(List<Long> products);
    Integer getStockOfProduct(Long productId);
    PageData<CartProductStock> getProductPage(
            Integer page,
            Integer size,
            String order,
            String category,
            String brand,
            List<Long> productIds
    );
    Map<Long, BigDecimal> getProductsPrice(List<Long> products);
}
