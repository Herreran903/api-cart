package com.api_cart.cart.domain.cart.usecase;

import com.api_cart.cart.domain.cart.api.ICartServicePort;
import com.api_cart.cart.domain.cart.exception.ex.*;
import com.api_cart.cart.domain.cart.model.Cart;
import com.api_cart.cart.domain.cart.model.CartProduct;
import com.api_cart.cart.domain.cart.model.CartProductStock;
import com.api_cart.cart.domain.cart.spi.ICartPersistencePort;
import com.api_cart.cart.domain.cart.spi.IFeignStockAdapterPort;
import com.api_cart.cart.domain.cart.spi.IFeignTransactionAdapterPort;
import com.api_cart.cart.domain.cart.spi.IJwtAdapterPort;
import com.api_cart.cart.domain.error.ErrorDetail;
import com.api_cart.cart.domain.page.PageData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.api_cart.cart.domain.cart.exception.CartExceptionMessage.*;
import static com.api_cart.cart.domain.cart.util.CartConstants.*;
import static com.api_cart.cart.domain.util.GlobalConstants.*;
import static com.api_cart.cart.domain.util.GlobalExceptionMessage.*;

public class CartUseCase implements ICartServicePort {

    private final ICartPersistencePort cartPersistencePort;
    private final IJwtAdapterPort jwtAdapterPort;
    private final IFeignStockAdapterPort feignStockAdapterPort;
    private final IFeignTransactionAdapterPort feignTransactionAdapterPort;

    public CartUseCase(ICartPersistencePort cartPersistencePort,
                       IJwtAdapterPort jwtAdapterPort,
                       IFeignStockAdapterPort feignStockAdapterPort,
                       IFeignTransactionAdapterPort feignTransactionAdapterPort) {
        this.cartPersistencePort = cartPersistencePort;
        this.jwtAdapterPort = jwtAdapterPort;
        this.feignStockAdapterPort = feignStockAdapterPort;
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
        Integer stock = feignStockAdapterPort.getStockOfProduct(productId);

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

        List<String> categories = feignStockAdapterPort.getListCategoriesOfProducts(products);

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

    private void validateCartPage(Integer page, Integer size, String order) {
        List<ErrorDetail> errors = new ArrayList<>();

        if (!(ASC.equalsIgnoreCase(order) || DESC.equalsIgnoreCase(order)))
            errors.add(new ErrorDetail(ORDER, INVALID_ORDER));

        if (page < MIN_PAGE_NUMBER)
            errors.add(new ErrorDetail(PAGE, NO_NEGATIVE_PAGE));

        if (size < MIN_PAGE_SIZE)
            errors.add(new ErrorDetail(SIZE, GREATER_ZERO_SIZE));

        if (!errors.isEmpty())
            throw new CartPageNotValidFieldException(INVALID_OBJECT, errors);
    }

    private void processCartProductStock(PageData<CartProductStock> cartProductStockPageData, List<Long> cartProducts, Cart cart) {
        cartProductStockPageData.getData().forEach(cartProductStock -> {
            if (!cartProducts.contains(cartProductStock.getProduct())) {
                return;
            }

            int index = cartProducts.indexOf(cartProductStock.getProduct());
            CartProduct cartProduct = cart.getProducts().get(index);

            if (cartProductStock.getQuantity() <= MIN_QUANTITY_VALUE || cartProductStock.getQuantity() < cartProduct.getQuantity()) {
                LocalDate restockDate = feignTransactionAdapterPort.getRestockDate(cartProductStock.getProduct());
                cartProductStock.setRestockDate(restockDate);
            }

            cartProductStock.setQuantity(cartProduct.getQuantity());
        });
    }

    private BigDecimal calculateTotalPrice(Cart cart, List<Long> products) {
        Map<Long, BigDecimal> prices = feignStockAdapterPort.getProductsPrice(products);

        BigDecimal total = BigDecimal.ZERO;

        for (CartProduct cartProduct : cart.getProducts()) {
            Long productId = cartProduct.getProduct();
            Integer quantity = cartProduct.getQuantity();

            BigDecimal productPrice = prices.get(productId);
            if (productPrice != null) {
                total = total.add(productPrice.multiply(BigDecimal.valueOf(quantity)));
            }
        }

        return total;
    }

    @Override
    public PageData<CartProductStock> getCartPage(Integer page, Integer size, String order, String category, String brand, String token) {
        validateCartPage(page, size, order);

        Long userId = getUserIdFromToken(token);
        Cart cart = getCartByUserId(userId);

        List<Long> cartProducts = cart.getProducts().stream().map(CartProduct::getProduct).toList();

        PageData<CartProductStock> cartProductStockPageData = feignStockAdapterPort.getProductPage(
                page, size, order, category, brand, cartProducts);

        processCartProductStock(cartProductStockPageData, cartProducts, cart);

        cartProductStockPageData.setTotal(calculateTotalPrice(cart, cartProducts));

        return cartProductStockPageData;
    }
}
