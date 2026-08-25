package com.api_cart.cart.app.cart.dto;

import com.api_cart.cart.domain.page.PageData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPageResponse {
    private PageData<CartProductStockResponse> page;
    private Integer total;
}
