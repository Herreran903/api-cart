package com.api_cart.cart.infra.cart.in;

import com.api_cart.cart.app.cart.dto.CartProductRequest;
import com.api_cart.cart.app.cart.dto.ProductIdRequest;
import com.api_cart.cart.app.cart.handler.ICartHandler;
import com.api_cart.cart.infra.exception.ExceptionDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.api_cart.cart.domain.util.GlobalConstants.HEADER_STRING;
import static com.api_cart.cart.infra.cart.util.CartSwaggerMessages.ADD_CART_REQUEST_EXAMPLE;
import static com.api_cart.cart.infra.cart.util.CartSwaggerMessages.REMOVE_CART_REQUEST_EXAMPLE;
import static com.api_cart.cart.infra.security.jwt.JwtUtility.extractJwt;
import static com.api_cart.cart.infra.util.SwaggerMessages.*;
import static com.api_cart.cart.infra.util.Urls.CART_URL;

@RestController
@RequestMapping(CART_URL)
@Validated
@RequiredArgsConstructor
public class CartController {

    private final ICartHandler cartHandler;

    private static final String ADD_CART_URL = "/add";
    private static final String REMOVE_CART_URL = "/remove";
    private static final String BUY_CART_URL = "/buy";

    @PostMapping(BUY_CART_URL)
    @PreAuthorize("hasRole(T(com.api_cart.cart.domain.role.util.RoleEnum).ROLE_CLIENT.toString())")
    ResponseEntity<Void> buyArticlesOnCart(){
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Add Article to Cart",
            description = "This endpoint allows adding an article to the user's cart. It requires a valid JWT token and a request body containing the product details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body to add an article to the cart",
                    content = @Content(
                            schema = @Schema(implementation = CartProductRequest.class),
                            examples = {
                                    @ExampleObject(value = ADD_CART_REQUEST_EXAMPLE)
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_200, description = RESPONSE_200_DESCRIPTION),
            @ApiResponse(responseCode = CODE_400, description = RESPONSE_400_DESCRIPTION,
                    content = @Content(schema = @Schema(implementation = ExceptionDetails.class))
            ),
            @ApiResponse(responseCode = CODE_500, description = RESPONSE_500_DESCRIPTION,
                    content = @Content(schema = @Schema(implementation = ExceptionDetails.class))
            )
    })
    @PostMapping(ADD_CART_URL)
    @PreAuthorize("hasRole(T(com.api_cart.cart.domain.role.util.RoleEnum).ROLE_CLIENT.toString())")
    ResponseEntity<Void> addArticleToCart(@Valid @RequestBody CartProductRequest cartProductRequest,
                                          @RequestHeader(HEADER_STRING) String authorization){
        String token = extractJwt(authorization);
        cartHandler.addProductToCart(cartProductRequest, token);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Delete Article of Cart",
            description = "This endpoint allows deleting an article to the user's cart. It requires a valid JWT token and a request body containing the product details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body to delete an article of the cart",
                    content = @Content(
                            schema = @Schema(implementation = CartProductRequest.class),
                            examples = {
                                    @ExampleObject(value = REMOVE_CART_REQUEST_EXAMPLE)
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_200, description = RESPONSE_200_DESCRIPTION),
            @ApiResponse(responseCode = CODE_400, description = RESPONSE_400_DESCRIPTION,
                    content = @Content(schema = @Schema(implementation = ExceptionDetails.class))
            ),
            @ApiResponse(responseCode = CODE_500, description = RESPONSE_500_DESCRIPTION,
                    content = @Content(schema = @Schema(implementation = ExceptionDetails.class))
            )
    })
    @PostMapping(REMOVE_CART_URL)
    @PreAuthorize("hasRole(T(com.api_cart.cart.domain.role.util.RoleEnum).ROLE_CLIENT.toString())")
    ResponseEntity<Void> deleteArticleOfCart(@Valid @RequestBody ProductIdRequest productIdRequest,
                                             @RequestHeader(HEADER_STRING) String authorization){
        String token = extractJwt(authorization);
        cartHandler.deleteArticleOfCart(productIdRequest, token);

        return ResponseEntity.ok().build();
    }
}
