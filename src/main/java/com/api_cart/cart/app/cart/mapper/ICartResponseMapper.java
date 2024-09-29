package com.api_cart.cart.app.cart.mapper;

import com.api_cart.cart.app.cart.dto.CartProductStockResponse;
import com.api_cart.cart.domain.cart.model.CartProductStock;
import com.api_cart.cart.domain.page.PageData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static com.api_cart.cart.domain.cart.util.CartConstants.PRODUCT;
import static com.api_cart.cart.domain.cart.util.CartConstants.PRODUCT_ID;

@Mapper(componentModel = "spring")
public interface ICartResponseMapper {
    @Mapping(target = PRODUCT_ID, source = PRODUCT)
    CartProductStockResponse toCartProductStockResponse(CartProductStock cartProductStock);
    PageData<CartProductStockResponse> toPageDataResponse(PageData<CartProductStock> pageData);
    PageData<CartProductStock> toPageData(PageData<CartProductStockResponse> page);
    default CartProductStock toCartProductStock(CartProductStockResponse cartProductStockResponse){
        if (cartProductStockResponse == null) return null;

        return new CartProductStock(
                cartProductStockResponse.getId(),
                cartProductStockResponse.getQuantity(),
                cartProductStockResponse.getName(),
                null);
    }
}
