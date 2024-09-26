package com.api_cart.cart.domain.cart.usecase;

import com.api_cart.cart.domain.cart.api.ICartServicePort;
import com.api_cart.cart.domain.cart.exception.ex.*;
import com.api_cart.cart.domain.cart.model.Cart;
import com.api_cart.cart.domain.cart.model.CartProduct;
import com.api_cart.cart.domain.cart.spi.ICartPersistencePort;
import com.api_cart.cart.domain.cart.spi.IFeignStockAdapterPort;
import com.api_cart.cart.domain.cart.spi.IFeignTransactionAdapterPort;
import com.api_cart.cart.domain.cart.spi.IJwtAdapterPort;
import com.api_cart.cart.domain.error.ErrorDetail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.api_cart.cart.domain.cart.exception.CartExceptionMessage.*;
import static com.api_cart.cart.domain.cart.util.CartConstants.*;
import static com.api_cart.cart.domain.util.GlobalExceptionMessage.INVALID_OBJECT;
import static com.api_cart.cart.domain.util.GlobalExceptionMessage.INVALID_USER;

public class CartUseCase implements ICartServicePort {

    private final ICartPersistencePort cartPersistencePort;
    private final IJwtAdapterPort jwtAdapterPort;
    private final IFeignStockAdapterPort feignAdapterPort;
    private final IFeignTransactionAdapterPort feignTransactionAdapterPort;

    public CartUseCase(ICartPersistencePort cartPersistencePort,
                       IJwtAdapterPort jwtAdapterPort,
                       IFeignStockAdapterPort feignAdapterPort,
                       IFeignTransactionAdapterPort feignTransactionAdapterPort) {
        this.cartPersistencePort = cartPersistencePort;
        this.jwtAdapterPort = jwtAdapterPort;
        this.feignAdapterPort = feignAdapterPort;
        this.feignTransactionAdapterPort = feignTransactionAdapterPort;
    }

    private void validateCartProduct(CartProduct cartProduct) {
        List<ErrorDetail> errors = new ArrayList<>();

        if (cartProduct.getProduct() == null)
            errors.add(new ErrorDetail(CART_PRODUCT, EMPTY_PRODUCT));

        if (cartProduct.getQuantity() == null)
            errors.add(new ErrorDetail(CART_QUANTITY, EMPTY_QUANTITY));

        if (!errors.isEmpty())
            throw new CartProductNotValidFieldException(INVALID_OBJECT, errors);

        if (cartProduct.getQuantity() <= MIN_QUANTITY_VALUE)
            errors.add(new ErrorDetail(CART_QUANTITY, MIN_QUANTITY));

        if (!errors.isEmpty())
            throw new CartProductNotValidFieldException(INVALID_OBJECT, errors);
    }

    private Long getUserIdFromToken(String token) {
        Long userId = jwtAdapterPort.getUserId(token);
        if (userId == null)
            throw new UserInvalidException(INVALID_USER);

        return userId;
    }

    private Cart createCart(Long userId){
        LocalDateTime date = LocalDateTime.now();

        Cart cart = new Cart(null, userId, date, date, List.of());
        return cartPersistencePort.createCart(cart);
    }

    private Cart getOrCreateCart(Long userId) {
        return cartPersistencePort.getCartByUserId(userId)
                .orElseGet(() -> createCart(userId));
    }

    private Integer getCurrentQuantityInCart(Cart cart, Long productId) {
        return cart.getProducts().stream()
                .filter(p -> p.getProduct().equals(productId))
                .map(CartProduct::getQuantity)
                .findFirst()
                .orElse(MIN_QUANTITY_VALUE);
    }

    private void validateStockProduct(Long productId, Integer quantity) {
        Integer stock = feignAdapterPort.getStockOfProduct(productId);

        if (stock == null || stock <= MIN_QUANTITY_VALUE){
            LocalDate restockDate = feignTransactionAdapterPort.getRestockDate(productId);
            throw new StockNotAvailableException(OUT_OF_STOCK, restockDate);
        }

        if (stock < quantity)
            throw new StockNotAvailableException(NOT_ENOUGH_STOCK);
    }

    private void updateCart(Cart currentCart, CartProduct cartProduct) {
        Optional<CartProduct> existingProduct = currentCart.getProducts().stream()
                .filter(p -> p.getProduct().equals(cartProduct.getProduct()))
                .findFirst();

        if (existingProduct.isPresent()) {
            CartProduct product = existingProduct.get();
            product.setQuantity(product.getQuantity() + cartProduct.getQuantity());
        } else {
            currentCart.getProducts().add(cartProduct);
        }

        currentCart.setUpdateDate(LocalDateTime.now());
    }

    private void validateCartCategories(Cart currentCart) {
        List<Long> products = currentCart.getProducts().stream()
                        .map(CartProduct::getProduct)
                                .toList();

        List<String> categories = feignAdapterPort.getListCategoriesOfProducts(products);

        Map<String, Long> categoryCount = categories.stream()
                .collect(Collectors.groupingBy(category -> category, Collectors.counting()));

        categoryCount.forEach((category, count) -> {
            if (count > MAX_CATEGORIES_VALUE)
                throw new CategoryLimitExceededException(EXCEEDED_CATEGORIES + category);
        });
    }

    @Override
    public void addProductToCart(CartProduct cartProduct, String token) {
        validateCartProduct(cartProduct);

        Long userId = getUserIdFromToken(token);
        Cart currentCart = getOrCreateCart(userId);

        Integer currentQuantityInCart = getCurrentQuantityInCart(currentCart, cartProduct.getProduct());

        validateStockProduct(cartProduct.getProduct(), cartProduct.getQuantity() + currentQuantityInCart);

        updateCart(currentCart, cartProduct);

        validateCartCategories(currentCart);

        cartPersistencePort.addProductToCart(currentCart);
    }

    private Cart getCartByUserId(Long userId) {
        return cartPersistencePort.getCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundByIdUserException(NO_FOUND_CART));
    }

    private CartProduct getProductFromCart(Cart currentCart, Long productId) {
        return currentCart.getProducts().stream()
                .filter(cartProduct -> cartProduct.getProduct().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundByIdException(NO_FOUND_PRODUCT));
    }

    @Override
    public void deleteArticleOfCart(Long productId, String token) {
        Long userId = getUserIdFromToken(token);

        Cart currentCart = getCartByUserId(userId);

        CartProduct productToDelete = getProductFromCart(currentCart, productId);

        currentCart.getProducts().remove(productToDelete);
        currentCart.setUpdateDate(LocalDateTime.now());

        cartPersistencePort.deleteArticleOfCart(currentCart);
    }
}
