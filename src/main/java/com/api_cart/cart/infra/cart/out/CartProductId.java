package com.api_cart.cart.infra.cart.out;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProductId implements Serializable {
    private Long cart;
    @Column(insertable=false, updatable=false)
    private Long product;
}
