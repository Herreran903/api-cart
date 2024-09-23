package com.api_cart.cart.infra.exception;

import com.api_cart.cart.domain.cart.exception.ex.CartProductNotValidFieldException;
import com.api_cart.cart.domain.cart.exception.ex.CategoryLimitExceededException;
import com.api_cart.cart.domain.cart.exception.ex.StockNotAvailableException;
import com.api_cart.cart.domain.cart.exception.ex.UserInvalidException;
import com.api_cart.cart.domain.error.ErrorDetail;
import com.api_cart.cart.domain.util.GlobalExceptionMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import feign.FeignException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.api_cart.cart.domain.util.GlobalExceptionMessage.*;

@ControllerAdvice
public class AdviceHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    //Cart
    @ExceptionHandler(CartProductNotValidFieldException.class)
    public ResponseEntity<ExceptionDetails> handleCartProductNotValidFieldException(CartProductNotValidFieldException ex) {
        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                "",
                LocalDateTime.now(),
                ex.getErrors()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);
    }

    @ExceptionHandler(UserInvalidException.class)
    public ResponseEntity<ExceptionDetails> handleUserInvalidException(UserInvalidException ex) {
        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                "",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(details);
    }

    @ExceptionHandler(StockNotAvailableException.class)
    public ResponseEntity<ExceptionDetails> handleStockNotAvailableException(StockNotAvailableException ex) {
        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                ex.getDetails(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);
    }

    @ExceptionHandler(CategoryLimitExceededException.class)
    public ResponseEntity<ExceptionDetails> handleCategoryLimitExceededException(CategoryLimitExceededException ex) {

        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                "",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);
    }

    //Auth
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDetails> handleAccessDeniedException(AccessDeniedException ex) {
        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                BAD_ROLE,
                "",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(details);
    }

    //Jwt
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ExceptionDetails> handleJwtException(JwtException ex) {
        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                INVALID_TOKEN,
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(details);
    }

    //Feign
    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public ResponseEntity<ExceptionDetails> handleFeignServiceUnavailableException(FeignException ex) {
        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                SERVICE_UNAVAILABLE,
                "",
                LocalDateTime.now(),
                null
        );

        return new ResponseEntity<>(details, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(FeignException.InternalServerError.class)
    public ResponseEntity<Object> handleFeignInternalServerErrorException(FeignException ex) {
        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                INTERNAL_ERROR,
                "",
                LocalDateTime.now(),
                null
        );

        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Object> handleFeignNotFoundException(FeignException ex) {
        try {
            Map<String, Object> details = objectMapper.readValue(ex.contentUTF8(), new TypeReference<Map<String, Object>>() {});
            return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(ex.contentUTF8(), HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<Object> handleFeignBadRequestException(FeignException ex) {
        try {
            Map<String, Object> details = objectMapper.readValue(ex.contentUTF8(), new TypeReference<Map<String, Object>>() {});
            return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ex.contentUTF8(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ExceptionDetails> handleConnectException(ConnectException ex) {
        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                SERVICE_UNAVAILABLE,
                null,
                LocalDateTime.now(),
                null
        );

        return new ResponseEntity<>(details, HttpStatus.SERVICE_UNAVAILABLE);
    }

    //General
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDetails> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        List<ErrorDetail> errorsDetails = new ArrayList<>();

        if (ex.getCause() instanceof MismatchedInputException mie) {
            String fieldName = !mie.getPath().isEmpty() ? mie.getPath().get(0).getFieldName() : "Unknown";
            String requiredType = mie.getTargetType() != null ? mie.getTargetType().getSimpleName() : "Unknown";
            String message = String.format(GlobalExceptionMessage.INVALID_TYPE_PARAM,
                    Objects.requireNonNull(fieldName),
                    Objects.requireNonNull(requiredType));

            errorsDetails.add(new ErrorDetail(fieldName, message));
        }

        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                GlobalExceptionMessage.INVALID_JSON,
                "",
                LocalDateTime.now(),
                errorsDetails
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<ErrorDetail> errorsDetails = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorDetail(fieldError.getField(), fieldError.getDefaultMessage()))
                .distinct()
                .toList();

        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                INVALID_OBJECT,
                "",
                LocalDateTime.now(),
                errorsDetails
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDetails> handleConstraintViolationException(ConstraintViolationException ex) {

        List<ErrorDetail> errorsDetails = ex.getConstraintViolations().stream()
                .map(constraintViolation -> {
                    String fieldName = constraintViolation.getPropertyPath().toString();
                    fieldName = fieldName.contains(".")
                            ? fieldName.substring(fieldName.lastIndexOf('.') + 1)
                            : fieldName;
                    return new ErrorDetail(fieldName, constraintViolation.getMessage());
                })
                .toList();

        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                GlobalExceptionMessage.INVALID_PARAMETERS,
                "",
                LocalDateTime.now(),
                errorsDetails
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);
    }
}
