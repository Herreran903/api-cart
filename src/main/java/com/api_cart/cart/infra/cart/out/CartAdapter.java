package com.api_cart.cart.infra.cart.out;

import com.api_cart.cart.domain.cart.spi.ICartPersistencePort;
import com.api_cart.cart.domain.cart.model.Cart;

import java.util.List;
import java.util.Optional;

public class CartAdapter implements ICartPersistencePort {

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;

    public CartAdapter(ICartRepository cartRepository, ICartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    public Optional<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUser(userId).map(cartMapper::toCart);
    }

    @Override
    public void deleteArticleOfCart(Cart cart) {
        addProductToCart(cart);
    }

    public Cart createCart(Cart cart) {
        return cartMapper.toCart(cartRepository.save(cartMapper.toEntity(cart)));
    }

    @Override
    public void addProductToCart(Cart cart) {
        CartEntity cartEntity = cartMapper.toEntity(cart);
        List<CartProductEntity> cartProductEntities = cartMapper.toProductEntities(cart.getProducts(), cartEntity);
        cartProductEntities.forEach(cartEntity::addProduct);

        cartRepository.save(cartEntity);
    }
}
