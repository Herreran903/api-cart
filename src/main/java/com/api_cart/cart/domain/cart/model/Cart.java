package com.api_cart.cart.domain.cart.model;

import java.time.LocalDateTime;
import java.util.List;

public class Cart {
    private Long id;
    private Long user;
    private LocalDateTime updateDate;
    private LocalDateTime createDate;
    private List<CartProduct> products;

    public Cart(Long id, Long user, LocalDateTime updateDate, LocalDateTime createDate, List<CartProduct> products) {
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

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public List<CartProduct> getProducts() {
        return products;
    }

    public void setProducts(List<CartProduct> products) {
        this.products = products;
    }
}
