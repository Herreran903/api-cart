package com.api_cart.cart.domain.cart.exception.ex;

import com.api_cart.cart.domain.error.ErrorDetail;

import java.util.List;

public class CartProductNotValidFieldException extends RuntimeException {
  private final transient List<ErrorDetail> errors;

  public CartProductNotValidFieldException(String message, List<ErrorDetail> errors) {
    super(message);
    this.errors = errors;
  }

  public List<ErrorDetail> getErrors() {
    return errors;
  }
}
