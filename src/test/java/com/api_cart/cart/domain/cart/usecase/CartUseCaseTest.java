package com.api_cart.cart.domain.cart.usecase;

import com.api_cart.cart.domain.cart.exception.ex.*;
import com.api_cart.cart.domain.cart.model.Cart;
import com.api_cart.cart.domain.cart.model.CartProduct;
import com.api_cart.cart.domain.cart.model.CartProductStock;
import com.api_cart.cart.domain.cart.spi.ICartPersistencePort;
import com.api_cart.cart.domain.cart.spi.IFeignStockAdapterPort;
import com.api_cart.cart.domain.cart.spi.IFeignTransactionAdapterPort;
import com.api_cart.cart.domain.cart.spi.IJwtAdapterPort;
import com.api_cart.cart.domain.page.PageData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.api_cart.cart.domain.cart.exception.CartExceptionMessage.*;
import static com.api_cart.cart.domain.cart.util.CartConstants.MIN_QUANTITY_VALUE;
import static com.api_cart.cart.domain.util.GlobalConstants.*;
import static com.api_cart.cart.domain.util.GlobalExceptionMessage.*;
import static com.api_cart.cart.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartUseCaseTest {

    @Mock
    private ICartPersistencePort cartPersistencePort;

    @Mock
    private IJwtAdapterPort jwtAdapterPort;

    @Mock
    private IFeignStockAdapterPort feignStockAdapterPort;

    @Mock
    private IFeignTransactionAdapterPort feignTransactionAdapterPort;

    @InjectMocks
    private CartUseCase cartUseCase;

    private Cart cart;
    private CartProduct cartProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cartProduct = new CartProduct(VALID_PRODUCT_ID, VALID_QUANTITY);
        cart = new Cart(VALID_CART_ID, VALID_USER_ID, VALID_CREATE_DATE, VALID_UPDATE_DATE, List.of(cartProduct));
    }

    @Test
    void addProductToCartShouldThrowExceptionWhenCartProductIsNull() {
        cartProduct.setProduct(null);

        CartProductNotValidFieldException exception = assertThrows(CartProductNotValidFieldException.class, () -> cartUseCase.addProductToCart(cartProduct, VALID_TOKEN));

        verify(jwtAdapterPort, never()).getUserId(anyString());
        verify(cartPersistencePort, never()).addProductToCart(any(Cart.class));
        assertFalse(exception.getErrors().isEmpty());
    }

    @Test
    void addProductToCartShouldThrowExceptionWhenCartQuantityIsNull() {
        cartProduct.setQuantity(null);

        CartProductNotValidFieldException exception = assertThrows(CartProductNotValidFieldException.class, () -> cartUseCase.addProductToCart(cartProduct, VALID_TOKEN));

        verify(jwtAdapterPort, never()).getUserId(anyString());
        verify(cartPersistencePort, never()).addProductToCart(any(Cart.class));
        assertFalse(exception.getErrors().isEmpty());
    }

    @Test
    void addProductToCartShouldThrowExceptionWhenCartQuantityIsInvalid() {
        cartProduct.setQuantity(MIN_QUANTITY_VALUE);

        CartProductNotValidFieldException exception = assertThrows(CartProductNotValidFieldException.class, () -> cartUseCase.addProductToCart(cartProduct, VALID_TOKEN));

        verify(jwtAdapterPort, never()).getUserId(anyString());
        verify(cartPersistencePort, never()).addProductToCart(any(Cart.class));
        assertFalse(exception.getErrors().isEmpty());
    }

    @Test
    void addProductToCartShouldThrowExceptionWhenTokenIsInvalid() {
        String invalidToken = "INVALID TOKEN";
        when(jwtAdapterPort.getUserId(invalidToken)).thenReturn(null);

        assertThrows(UserInvalidException.class, () -> cartUseCase.addProductToCart(cartProduct, invalidToken));

        verify(jwtAdapterPort, times(1)).getUserId(invalidToken);
        verify(cartPersistencePort, never()).addProductToCart(any(Cart.class));
    }

    @Test
    void addProductToCartShouldUpdateCartIfStockIsValid() {
        when(jwtAdapterPort.getUserId(VALID_TOKEN)).thenReturn(1L);
        when(cartPersistencePort.getCartByUserId(1L)).thenReturn(Optional.of(cart));
        when(feignStockAdapterPort.getStockOfProduct(cartProduct.getProduct())).thenReturn(5);

        cartUseCase.addProductToCart(cartProduct, VALID_TOKEN);

        verify(cartPersistencePort, times(1)).addProductToCart(cart);
        assertEquals(2, cart.getProducts().get(0).getQuantity());
    }

    @Test
    void addProductToCartShouldThrowStockNotAvailableException() {
        when(jwtAdapterPort.getUserId(VALID_TOKEN)).thenReturn(1L);
        when(cartPersistencePort.getCartByUserId(1L)).thenReturn(Optional.of(cart));
        when(feignStockAdapterPort.getStockOfProduct(cartProduct.getProduct())).thenReturn(1);

        assertThrows(StockNotAvailableException.class, () -> cartUseCase.addProductToCart(cartProduct, VALID_TOKEN));

        verify(cartPersistencePort, never()).addProductToCart(cart);
    }

    @Test
    void addProductToCartShouldThrowOutOfStockZeroException() {
        when(jwtAdapterPort.getUserId(VALID_TOKEN)).thenReturn(VALID_USER_ID);
        when(cartPersistencePort.getCartByUserId(VALID_USER_ID)).thenReturn(Optional.of(cart));
        when(feignStockAdapterPort.getStockOfProduct(cartProduct.getProduct())).thenReturn(0);
        when(feignTransactionAdapterPort.getRestockDate(VALID_PRODUCT_ID)).thenReturn(LocalDate.now());

        StockNotAvailableException exception = assertThrows(StockNotAvailableException.class, () -> cartUseCase.addProductToCart(cartProduct, VALID_TOKEN));

        verify(cartPersistencePort, never()).addProductToCart(cart);
        assertEquals(OUT_OF_STOCK,exception.getMessage());
        assertEquals(DATE_RESTOCK + LocalDate.now(), exception.getDetails());
    }

    @Test
    void addProductToCartShouldThrowOutOfStockNullException() {
        when(jwtAdapterPort.getUserId(VALID_TOKEN)).thenReturn(VALID_USER_ID);
        when(cartPersistencePort.getCartByUserId(VALID_USER_ID)).thenReturn(Optional.of(cart));
        when(feignStockAdapterPort.getStockOfProduct(cartProduct.getProduct())).thenReturn(null);
        when(feignTransactionAdapterPort.getRestockDate(VALID_PRODUCT_ID)).thenReturn(LocalDate.now());

        StockNotAvailableException exception = assertThrows(StockNotAvailableException.class, () -> cartUseCase.addProductToCart(cartProduct, VALID_TOKEN));

        verify(cartPersistencePort, never()).addProductToCart(cart);
        assertEquals(OUT_OF_STOCK,exception.getMessage());
    }

    @Test
    void addProductToCartShouldCreateNewCartIfNoneExists() {
        when(jwtAdapterPort.getUserId(VALID_TOKEN)).thenReturn(VALID_USER_ID);
        when(cartPersistencePort.getCartByUserId(VALID_USER_ID)).thenReturn(Optional.empty());
        when(feignStockAdapterPort.getStockOfProduct(cartProduct.getProduct())).thenReturn(5);

        when(cartPersistencePort.createCart(any(Cart.class))).thenReturn(cart);

        cartUseCase.addProductToCart(cartProduct, VALID_TOKEN);

        verify(cartPersistencePort, times(1)).createCart(any(Cart.class));
        verify(cartPersistencePort, times(1)).addProductToCart(cart);
    }

    @Test
    void updateCartShouldIncreaseQuantityIfProductExists() {
        cart.setProducts(new ArrayList<>());

        when(jwtAdapterPort.getUserId(VALID_TOKEN)).thenReturn(VALID_USER_ID);
        when(cartPersistencePort.getCartByUserId(VALID_USER_ID)).thenReturn(Optional.ofNullable(cart));
        when(feignStockAdapterPort.getStockOfProduct(cartProduct.getProduct())).thenReturn(5);


        cartUseCase.addProductToCart(cartProduct, VALID_TOKEN);

        verify(cartPersistencePort, times(1)).addProductToCart(cart);
    }


    @Test
    void addProductToCartShouldThrowCategoryLimitExceededException() {
        when(jwtAdapterPort.getUserId(VALID_TOKEN)).thenReturn(1L);
        when(cartPersistencePort.getCartByUserId(1L)).thenReturn(Optional.of(cart));
        when(feignStockAdapterPort.getStockOfProduct(cartProduct.getProduct())).thenReturn(5);
        when(feignStockAdapterPort.getListCategoriesOfProducts(anyList()))
                .thenReturn(List.of("category1", "category1", "category1", "category1"));

        assertThrows(CategoryLimitExceededException.class, () -> cartUseCase.addProductToCart(cartProduct, VALID_TOKEN));

        verify(cartPersistencePort, never()).addProductToCart(cart);
    }

    @Test
    void deleteArticleOfCartShouldRemoveProductWhenProductExists() {
        cart.setProducts(new ArrayList<>());
        cart.getProducts().add(cartProduct);
        when(jwtAdapterPort.getUserId(VALID_TOKEN)).thenReturn(VALID_USER_ID);
        when(cartPersistencePort.getCartByUserId(VALID_USER_ID)).thenReturn(Optional.of(cart));

        cartUseCase.deleteArticleOfCart(VALID_PRODUCT_ID, VALID_TOKEN);

        assertTrue(cart.getProducts().isEmpty());
        verify(cartPersistencePort, times(1)).deleteArticleOfCart(cart);
    }

    @Test
    void deleteArticleOfCartShouldThrowProductNotFoundWhenProductDoesNotExist() {
        cart.setProducts(new ArrayList<>());
        when(jwtAdapterPort.getUserId(VALID_TOKEN)).thenReturn(VALID_USER_ID);
        when(cartPersistencePort.getCartByUserId(VALID_USER_ID)).thenReturn(Optional.of(cart));

        ProductNotFoundByIdException exception = assertThrows(ProductNotFoundByIdException.class, () -> {
            cartUseCase.deleteArticleOfCart(VALID_PRODUCT_ID, VALID_TOKEN);
        });

        assertEquals(NO_FOUND_PRODUCT, exception.getMessage());
        verify(cartPersistencePort, never()).deleteArticleOfCart(any());
    }

    @Test
    void deleteArticleOfCartShouldThrowCartNotFoundWhenCartDoesNotExist() {
        when(jwtAdapterPort.getUserId(VALID_TOKEN)).thenReturn(VALID_USER_ID);
        when(cartPersistencePort.getCartByUserId(VALID_USER_ID)).thenReturn(Optional.empty());

        CartNotFoundByIdUserException exception = assertThrows(CartNotFoundByIdUserException.class, () -> {
            cartUseCase.deleteArticleOfCart(VALID_PRODUCT_ID, VALID_TOKEN);
        });

        assertEquals(NO_FOUND_CART, exception.getMessage());
        verify(cartPersistencePort, never()).deleteArticleOfCart(any());
    }

    @Test
    void getCartPageInvalidOrderThrowsException() {
        String invalidOrder = "INVALID";

        CartPageNotValidFieldException exception = assertThrows(
                CartPageNotValidFieldException.class,
                () -> cartUseCase.getCartPage(VALID_PAGE, VALID_SIZE, invalidOrder, null, null, VALID_TOKEN)
        );

        assertTrue(exception.getErrors().stream()
                .anyMatch(error -> error.getField().equals(ORDER) && error.getMessage().equals(INVALID_ORDER)));
    }

    @Test
    void getCartPageInvalidPageThrowsException() {
        Integer invalidPage = -1;

        CartPageNotValidFieldException exception = assertThrows(
                CartPageNotValidFieldException.class,
                () -> cartUseCase.getCartPage(invalidPage, VALID_SIZE, ASC, null, null, VALID_TOKEN)
        );

        assertTrue(exception.getErrors().stream()
                .anyMatch(error -> error.getField().equals(PAGE) && error.getMessage().equals(NO_NEGATIVE_PAGE)));
    }

    @Test
    void getCartPageInvalidSizeThrowsException() {
        Integer invalidSize = 0;

        CartPageNotValidFieldException exception = assertThrows(
                CartPageNotValidFieldException.class,
                () -> cartUseCase.getCartPage(VALID_PAGE, invalidSize, ASC, null, null, VALID_TOKEN)
        );

        assertTrue(exception.getErrors().stream()
                .anyMatch(error -> error.getField().equals(SIZE) && error.getMessage().equals(GREATER_ZERO_SIZE)));
    }

    @Test
    void getCartPageWhenStockIsLessThanCartQuantityShouldGetRestockDate() {
        Cart mockCart = new Cart(VALID_CART_ID, VALID_USER_ID, VALID_CREATE_DATE, VALID_UPDATE_DATE, List.of(
                new CartProduct(VALID_PRODUCT_ID, VALID_QUANTITY)
        ));
        List<Long> productIds = List.of(VALID_PRODUCT_ID);

        PageData<CartProductStock> mockPageData = new PageData<>(
                List.of(new CartProductStock(
                        VALID_PRODUCT_ID,
                        VALID_QUANTITY - 1,
                        null,
                        null)),
                0,
                1,
                true,
                true,
                false,
                false
        );

        LocalDate mockRestockDate = LocalDate.now().plusDays(5);

        when(jwtAdapterPort.getUserId(VALID_TOKEN)).thenReturn(VALID_USER_ID);
        when(cartPersistencePort.getCartByUserId(VALID_USER_ID)).thenReturn(Optional.of(mockCart));
        when(feignStockAdapterPort.getProductPage(VALID_PAGE, VALID_SIZE, ASC, null, null, productIds))
                .thenReturn(mockPageData);
        when(feignStockAdapterPort.getProductsPrice(anyList())).thenReturn(Map.of(VALID_PRODUCT_ID, new BigDecimal("100")));
        when(feignTransactionAdapterPort.getRestockDate(VALID_PRODUCT_ID)).thenReturn(mockRestockDate);

        PageData<CartProductStock> result = cartUseCase.getCartPage(VALID_PAGE, VALID_SIZE, ASC, null, null, VALID_TOKEN);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(new BigDecimal("100"), result.getTotal());

        assertEquals(mockRestockDate, result.getData().get(0).getRestockDate());

        verify(jwtAdapterPort).getUserId(VALID_TOKEN);
        verify(cartPersistencePort).getCartByUserId(VALID_USER_ID);
        verify(feignStockAdapterPort).getProductPage(VALID_PAGE, VALID_SIZE, ASC, null, null, productIds);
        verify(feignStockAdapterPort).getProductsPrice(productIds);
        verify(feignTransactionAdapterPort).getRestockDate(VALID_PRODUCT_ID);
    }

    @Test
    void getCartPageValidRequestSuccessfulResponse() {
        Cart mockCart = new Cart(VALID_CART_ID, VALID_USER_ID, VALID_CREATE_DATE, VALID_UPDATE_DATE, List.of(
                new CartProduct(VALID_PRODUCT_ID, VALID_QUANTITY)
        ));
        List<Long> productIds = List.of(VALID_PRODUCT_ID);

        CartProductStock cartProductStock = new CartProductStock(
                VALID_PRODUCT_ID,
                VALID_QUANTITY,
                null,
                null);

        cartProductStock.setName("PRODUCT");

        PageData<CartProductStock> mockPageData = new PageData<>(
                List.of(cartProductStock),
                0,
                1,
                true,
                true,
                false,
                false
        );

        when(jwtAdapterPort.getUserId(VALID_TOKEN)).thenReturn(VALID_USER_ID);
        when(cartPersistencePort.getCartByUserId(VALID_USER_ID)).thenReturn(Optional.of(mockCart));
        when(feignStockAdapterPort.getProductPage(VALID_PAGE, VALID_SIZE, ASC, null, null, productIds))
                .thenReturn(mockPageData);
        when(feignStockAdapterPort.getProductsPrice(anyList())).thenReturn(Map.of(VALID_PRODUCT_ID, new BigDecimal("100")));

        PageData<CartProductStock> result = cartUseCase.getCartPage(VALID_PAGE, VALID_SIZE, ASC, null, null, VALID_TOKEN);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(new BigDecimal("100"), result.getTotal());
        assertEquals(cartProductStock.getName(), result.getData().get(0).getName());

        verify(jwtAdapterPort).getUserId(VALID_TOKEN);
        verify(cartPersistencePort).getCartByUserId(VALID_USER_ID);
        verify(feignStockAdapterPort).getProductPage(VALID_PAGE, VALID_SIZE, ASC, null, null, productIds);
        verify(feignStockAdapterPort).getProductsPrice(productIds);
    }
}