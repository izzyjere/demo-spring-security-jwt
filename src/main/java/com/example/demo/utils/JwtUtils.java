package com.example.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtils {

    @Value("${app.key-store}")
    private String keyStorePath;
    @Value("${app.key-store-password}")
    private String keyStorePassword;
    @Value("${app.key-alias}")
    private String keyAlias;

    @Value("${app.token-expiration}")
    private long tokenValidityMinutes;

    public String generateToken(UserDetails user, Map<String, Object> moreClaims) {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }
        var pubKey = getKey(true);
        var privKey = getKey(false);
        if (pubKey == null || privKey == null) {
            throw new IllegalArgumentException("Public key and private key is null");
        }
        return Jwts
                .builder()
                .signWith(privKey)
                .claims(moreClaims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (tokenValidityMinutes * 60 * 1000)))
                .compact();
    }

    private Key getKey(boolean isPublic) {
        try (var storeInputStream = new FileInputStream(new File(keyStorePath))) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(storeInputStream, keyStorePassword.toCharArray());
            if (isPublic) {
                return keyStore.getCertificate(keyAlias).getPublicKey();
            }
            return keyStore.getKey(keyAlias, keyStorePassword.toCharArray());
        } catch (Exception e) {
            log.error("get public key error", e);
            return null;
        }
    }

    public String extractUsername(String token) {
        if (token == null) {
            throw new IllegalArgumentException("token is null");
        }
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails user) {
        var username = extractUsername(token);
        var expiration = extractClaim(token, Claims::getExpiration);
        return username.equals(user.getUsername()) && expiration.before(new Date());
    }

    private Claims extractClaims(String token) {
        var pubKey = (PublicKey) getKey(true);
        if (pubKey == null) {
            throw new IllegalArgumentException("pubKey is null");
        }
        return Jwts.parser()
                .verifyWith(pubKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        var claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }
}
