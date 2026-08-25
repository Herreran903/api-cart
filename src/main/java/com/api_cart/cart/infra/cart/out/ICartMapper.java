package com.api_cart.cart.infra.cart.out;

import com.api_cart.cart.domain.cart.model.Cart;
import com.api_cart.cart.domain.cart.model.CartProduct;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static com.api_cart.cart.domain.cart.util.CartConstants.*;

@Mapper(componentModel = "spring")
public interface ICartMapper {
    @Mapping(target = PRODUCTS, ignore = true)
    CartEntity toEntity(Cart cart);

    Cart toCart(CartEntity cartEntity);

    CartProductEntity toProductEntity(CartProduct cartProduct);

    @Mapping(target = PRODUCT, source = PRODUCT_ID + "." + PRODUCT)
    CartProduct toCartProduct(CartProductEntity cartProductEntity);

    default List<CartProductEntity> toProductEntities(List<CartProduct> cartProducts, @Context CartEntity cartEntity) {
        return cartProducts.stream()
                .map( cartProduct -> {
                    CartProductId id = new CartProductId(cartEntity.getId(), cartProduct.getProduct());
                    CartProductEntity cartProductEntity = toProductEntity(cartProduct);
                    cartProductEntity.setId(id);
                    return cartProductEntity;
                })
                .toList();
    }
}