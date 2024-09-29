package com.api_cart.cart.app.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartProductStockResponse {
    private Long id;
    private String name;
    private Integer quantity;
}
