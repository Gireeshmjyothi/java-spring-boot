package com.example.practice.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.practice.config.AppConfig;
import com.example.practice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final String jwtSigningKey;
    private final AppConfig appConfig;

    public JwtService(@Value("${token.signing.key}") String jwtSigningKey, AppConfig appConfig) {
        this.jwtSigningKey = jwtSigningKey;
        this.appConfig = appConfig;
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateJWTToken(User user, Date expiryTime) {
        return JWT.create().withSubject(user.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(expiryTime)
                .sign(Algorithm.HMAC256(jwtSigningKey));
    }

    public String generateToken(UserDetails userDetails, Date expiryTime) {
        return JWT.create().withSubject(userDetails.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(expiryTime)
                .sign(Algorithm.HMAC256(jwtSigningKey));
    }
    public long extractIssuedAt(String token){
        return JWT.decode(token).getIssuedAt().getTime();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
    }

    private Key getSigningKey() {
        return new SecretKeySpec(jwtSigningKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }
}
