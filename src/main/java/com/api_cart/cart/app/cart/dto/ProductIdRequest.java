package com.api_cart.cart.app.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.api_cart.cart.domain.cart.exception.CartExceptionMessage.EMPTY_PRODUCT;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductIdRequest {
    @NotNull(message = EMPTY_PRODUCT)
    private Long product;
}
