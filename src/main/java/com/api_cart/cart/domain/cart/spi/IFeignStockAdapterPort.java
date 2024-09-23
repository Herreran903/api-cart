package com.api_cart.cart.domain.cart.spi;

import java.util.List;

public interface IFeignStockAdapterPort {
    List<String> getListCategoriesOfProducts(List<Long> products);
    Integer getStockOfProduct(Long productId);
}
