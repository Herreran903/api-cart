package com.api_cart.cart.infra.cart.out;

import jakarta.persistence.*;
import lombok.*;

import static com.api_cart.cart.domain.cart.util.CartConstants.CART_PRODUCT_TABLE_NAME;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = CART_PRODUCT_TABLE_NAME)
public class CartProductEntity {
    @EmbeddedId
    private CartProductId id = new CartProductId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cart")
    @JoinColumn(name = "cart")
    private CartEntity cart;

    @Column(nullable = false)
    private Integer quantity;
}
