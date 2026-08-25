package com.api_cart.cart.domain.cart.exception.ex;

import java.time.LocalDate;

import static com.api_cart.cart.domain.cart.exception.CartExceptionMessage.DATE_RESTOCK;

public class StockNotAvailableException extends RuntimeException {
    private final String details;

    public StockNotAvailableException(String message) {
        super(message);
        this.details = "";
    }

    public StockNotAvailableException(String message, LocalDate restockDate) {
        super(message);
        this.details = DATE_RESTOCK + restockDate;
    }

    public String getDetails() {
        return details;
    }
}
