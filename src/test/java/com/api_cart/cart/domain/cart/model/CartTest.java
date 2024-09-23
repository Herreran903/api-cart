package com.api_cart.cart.domain.cart.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.api_cart.cart.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {


    @Test
    void shouldUpdateCartSuccessfully(){
        Cart cart = new Cart(null, null, null, null, null);

        cart.setId(VALID_CART_ID);
        cart.setUser(VALID_USER_ID);
        cart.setUpdateDate(VALID_UPDATE_DATE);
        cart.setCreateDate(VALID_CREATE_DATE);
        cart.setProducts(List.of(new CartProduct(VALID_PRODUCT_ID, VALID_QUANTITY)));

        assertEquals(VALID_CART_ID, cart.getId());
        assertEquals(VALID_USER_ID, cart.getUser());
        assertEquals(VALID_UPDATE_DATE, cart.getUpdateDate());
        assertEquals(VALID_CREATE_DATE, cart.getCreateDate());
        assertEquals(VALID_PRODUCT_ID, cart.getProducts().get(0).getProduct());
    }
}