package com.api_cart.cart.infra.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.api_cart.cart.domain.util.GlobalConstants.HEADER_STRING;
import static com.api_cart.cart.domain.util.GlobalConstants.TOKEN_PREFIX;
import static com.api_cart.cart.infra.security.jwt.JwtUtility.extractJwt;

public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request != null) {
            String token = extractJwt(request.getHeader(HEADER_STRING));
            requestTemplate.header(HEADER_STRING, TOKEN_PREFIX + token);
        }
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}
