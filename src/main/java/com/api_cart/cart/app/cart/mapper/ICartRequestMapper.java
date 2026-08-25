package com.api_cart.cart.app.cart.mapper;

import com.api_cart.cart.app.cart.dto.CartProductRequest;
import com.api_cart.cart.domain.cart.model.CartProduct;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICartRequestMapper {
    CartProduct toCartProduct(CartProductRequest cartProductRequest);
}
