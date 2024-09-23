package com.api_cart.cart.app.cart.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductIdListRequest {
    @NotEmpty
    @NotNull
    private List<Long> products;
}
