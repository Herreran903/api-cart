package com.api_cart.cart.domain.cart.model;

import java.time.LocalDate;

public class CartProductStock extends CartProduct {
    private String name;
    private LocalDate restockDate;

    public CartProductStock(Long product, Integer quantity, String name, LocalDate restockDate) {
        super(product, quantity);
        this.name = name;
        this.restockDate = restockDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getRestockDate() {
        return restockDate;
    }

    public void setRestockDate(LocalDate restockDate) {
        this.restockDate = restockDate;
    }
}
