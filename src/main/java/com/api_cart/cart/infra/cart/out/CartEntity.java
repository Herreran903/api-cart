package com.api_cart.cart.infra.cart.out;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.api_cart.cart.domain.cart.util.CartConstants.CART;
import static com.api_cart.cart.domain.cart.util.CartConstants.CART_TABLE_NAME;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = CART_TABLE_NAME)
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long user;

    @Column
    private LocalDateTime updateDate;

    @Column
    private LocalDateTime createDate;

    @OneToMany(mappedBy = CART, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProductEntity> products = new ArrayList<>();

    public void addProduct(CartProductEntity product) {
        products.add(product);
        product.setCart(this);
    }
}
