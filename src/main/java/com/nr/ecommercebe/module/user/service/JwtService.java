package com.nr.ecommercebe.module.user.service;

import com.nr.ecommercebe.common.utils.PemUtil;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Value("${jwt.private-key-path}")
    private Resource privateKeyResource;

    @Value("${jwt.public-key-path}")
    private Resource publicKeyResource;

    @Value("${jwt.issuer}")
    private String issuer;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        this.privateKey = PemUtil.loadPrivateKey(privateKeyResource);
        this.publicKey = PemUtil.loadPublicKey(publicKeyResource);
    }

    public String generateAccessToken(String userId) {
        return buildToken(userId, accessExpiration, Map.of("type", "access"));
    }

    public String generateRefreshToken(String userId) {
        return buildToken(userId, refreshExpiration, Map.of("type", "refresh"));
    }

    private String buildToken(String userId, long expirationMillis, Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUserId(String token) {
        return parseToken(token).getPayload().getSubject();
    }

    public boolean isAccessToken(String token) {
        return "access".equals(parseToken(token).getPayload().get("type"));
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(parseToken(token).getPayload().get("type"));
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token);
    }
}
