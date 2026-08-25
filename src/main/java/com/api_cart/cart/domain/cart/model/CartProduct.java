package com.api_cart.cart.domain.cart.model;

public class CartProduct {
    private Long product;
    private Integer quantity;

    public CartProduct(Long product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }
}
