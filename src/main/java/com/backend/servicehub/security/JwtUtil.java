package com.backend.servicehub.security;

import com.backend.servicehub.common.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token.expiration-time}")
    private Long jwtAccessTokenExpirationMs;

    @Value("${jwt.refresh-token.expiration-time}")
    private Long jwtRefreshTokenExpirationMs;

    public JwtUtil() {

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            jwtSecret = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateAccessToken(String email) {
        return buildToken(email, jwtAccessTokenExpirationMs, TokenType.ACCESS);
    }

    public String generateRefreshToken(String email) {
        return buildToken(email, jwtRefreshTokenExpirationMs, TokenType.REFRESH);
    }

    private String buildToken(String email, Long expirationMs, TokenType tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenType", tokenType.name());
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean validateToken(String token, UserDetails userDetails, TokenType tokenType) {
        return validateToken(token, userDetails) && tokenType == extractTokenType(token);
    }

    public TokenType extractTokenType(String token) {
        String tokenTypeValue = extractClaim(token, claims -> claims.get("tokenType", String.class));
        if (tokenTypeValue == null) {
            return TokenType.ACCESS;
        }
        return TokenType.valueOf(tokenTypeValue);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
