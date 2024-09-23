package com.api_cart.cart.domain.cart.model;

import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
public class Cart {
    private Long id;
    private Long user;
    private LocalDate updateDate;
    private LocalDate createDate;
    private List<CartProduct> products;

    public Cart(Long id, Long user, LocalDate updateDate, LocalDate createDate, List<CartProduct> products) {
        this.id = id;
        this.user = user;
        this.updateDate = updateDate;
        this.createDate = createDate;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public List<CartProduct> getProducts() {
        return products;
    }

    public void setProducts(List<CartProduct> products) {
        this.products = products;
    }
}
