package com.api_cart.cart.app.cart.handler;

import com.api_cart.cart.app.cart.dto.CartProductRequest;
import com.api_cart.cart.app.cart.dto.CartProductStockResponse;
import com.api_cart.cart.app.cart.dto.ProductIdRequest;
import com.api_cart.cart.app.cart.mapper.ICartRequestMapper;
import com.api_cart.cart.app.cart.mapper.ICartResponseMapper;
import com.api_cart.cart.domain.cart.api.ICartServicePort;
import com.api_cart.cart.domain.page.PageData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartHandler implements ICartHandler {
    private final ICartServicePort cartServicePort;
    private final ICartRequestMapper cartRequestMapper;
    private final ICartResponseMapper cartResponseMapper;

    @Override
    public void addProductToCart(CartProductRequest cartProductRequest, String token) {
        cartServicePort.addProductToCart(cartRequestMapper.toCartProduct(cartProductRequest), token);
    }

    @Override
    public void deleteArticleOfCart(ProductIdRequest product, String token) {
        cartServicePort.deleteArticleOfCart(product.getProduct(), token);
    }

    @Override
    public PageData<CartProductStockResponse> getCartPage(
            Integer page,
            Integer size,
            String order,
            String category,
            String brand,
            String token
    ){
        return cartResponseMapper.toPageDataResponse(cartServicePort.getCartPage(
                page,
                size,
                order,
                category,
                brand,
                token
        ));
    }
}
