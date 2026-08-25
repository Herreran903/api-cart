package com.api_cart.cart.app.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.api_cart.cart.domain.cart.exception.CartExceptionMessage.*;
import static com.api_cart.cart.domain.cart.util.CartConstants.MIN_QUANTITY_VALUE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartProductRequest {
    @NotNull(message = EMPTY_PRODUCT)
    private Long product;

    @NotNull(message = EMPTY_QUANTITY)
    @Min(value = MIN_QUANTITY_VALUE, message = MIN_QUANTITY)
    private Integer quantity;
}
