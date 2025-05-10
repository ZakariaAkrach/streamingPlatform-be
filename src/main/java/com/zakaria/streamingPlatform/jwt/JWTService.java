package com.zakaria.streamingPlatform.jwt;

import com.zakaria.streamingPlatform.response.ResponseToken;
import com.zakaria.streamingPlatform.utils.Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${secret-key}")
    private String secretKey;

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        long FIFTEEN_MINUTES = 900_000;

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + FIFTEEN_MINUTES))
                .and()
                .signWith(getKey())
                .compact()
                ;
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
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
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public ResponseToken refreshToken(String bearer) {
        ResponseToken response = new ResponseToken();
        String tokenExtracted = bearer.substring(7);
        String email = extractUsername(tokenExtracted);
        String generatedToken = generateToken(email);

        if (generatedToken.isEmpty()) {
            return Utils.createResponseToken(HttpStatus.BAD_REQUEST.value(), "Error refresh token", null);
        }
        return Utils.createResponseToken(HttpStatus.OK.value(), "Token refreshed successfully", generatedToken);
    }
}
