package com.api_cart.cart.app.cart.handler;

import com.api_cart.cart.app.cart.dto.CartProductRequest;
import com.api_cart.cart.app.cart.dto.CartProductStockResponse;
import com.api_cart.cart.app.cart.dto.ProductIdRequest;
import com.api_cart.cart.domain.page.PageData;

public interface ICartHandler {
    void addProductToCart(CartProductRequest product, String token);
    void deleteArticleOfCart(ProductIdRequest product, String token);
    PageData<CartProductStockResponse> getCartPage(
            Integer page,
            Integer size,
            String order,
            String category,
            String brand,
            String token);
}
