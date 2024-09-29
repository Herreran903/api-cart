package com.api_cart.cart.infra.feign.stock;

import com.api_cart.cart.app.cart.dto.ProductIdListRequest;
import com.api_cart.cart.app.cart.dto.ProductIdRequest;
import com.api_cart.cart.app.cart.mapper.ICartResponseMapper;
import com.api_cart.cart.domain.cart.model.CartProductStock;
import com.api_cart.cart.domain.cart.spi.IFeignStockAdapterPort;
import com.api_cart.cart.domain.page.PageData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FeignStockAdapter implements IFeignStockAdapterPort {
    private final StockFeignClient stockFeignClient;
    private final ICartResponseMapper cartResponseMapper;

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

    @Override
    public Map<Long, BigDecimal> getProductsPrice(List<Long> products){
        ProductIdListRequest productIdListRequest = new ProductIdListRequest(products);
        return stockFeignClient.getProductsPrice(productIdListRequest);
    }

    @Override
    public PageData<CartProductStock> getProductPage(
            Integer page,
            Integer size,
            String order,
            String category,
            String brand,
            List<Long> products
    ){
        ProductIdListRequest productIdListRequest = new ProductIdListRequest(products);

        return cartResponseMapper.toPageData(stockFeignClient.getProductPage(
                page,
                size,
                order,
                category,
                brand,
                productIdListRequest
        ));
    }
}
