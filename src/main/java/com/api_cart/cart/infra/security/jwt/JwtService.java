package com.api_cart.cart.infra.security.jwt;

import com.api_cart.cart.domain.cart.spi.IJwtAdapterPort;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.function.Function;

import static com.api_cart.cart.domain.util.GlobalConstants.ROLES;
import static com.api_cart.cart.domain.util.GlobalConstants.USER_ID;

@Service
public class JwtService implements IJwtAdapterPort {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Override
    public Long getUserId(String token){
        return extractId(token);
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractId(String token) {
        return extractClaim(token, claims -> claims.get(USER_ID, Long.class));
    }

    public GrantedAuthority extractAuthorities(String token) {
        String role = extractClaim(token, claims -> claims.get(ROLES, String.class));

        return new SimpleGrantedAuthority(role);
    }

    private Claims extractAllClaims(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}