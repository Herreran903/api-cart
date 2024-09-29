package com.api_cart.cart.domain.cart.api;

import com.api_cart.cart.domain.cart.model.CartProduct;
import com.api_cart.cart.domain.cart.model.CartProductStock;
import com.api_cart.cart.domain.page.PageData;

public interface ICartServicePort {
    void addProductToCart(CartProduct cartProduct, String token);
    void deleteArticleOfCart(Long productId, String token);
    PageData<CartProductStock> getCartPage(Integer page, Integer size, String order, String category, String brand, String token);
}
