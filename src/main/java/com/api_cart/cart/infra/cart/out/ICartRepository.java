package com.api_cart.cart.infra.cart.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByUser(Long userId);
}
