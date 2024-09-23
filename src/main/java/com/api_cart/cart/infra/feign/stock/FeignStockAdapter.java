package com.api_cart.cart.infra.feign.stock;

import com.api_cart.cart.app.cart.dto.ProductIdListRequest;
import com.api_cart.cart.app.cart.dto.ProductIdRequest;
import com.api_cart.cart.domain.cart.spi.IFeignStockAdapterPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeignStockAdapter implements IFeignStockAdapterPort {
    private final StockFeignClient stockFeignClient;

    @Override
    public List<String> getListCategoriesOfProducts(List<Long> products) {
        ProductIdListRequest productIdListRequest = new ProductIdListRequest(products);
        return stockFeignClient.getListCategoriesOfProducts(productIdListRequest).getCategories();
    }

    @Override
    public Integer getStockOfProduct(Long id){
        ProductIdRequest productIdRequest = new ProductIdRequest(id);
        return stockFeignClient.getStockOfProduct(productIdRequest);
    }
}
