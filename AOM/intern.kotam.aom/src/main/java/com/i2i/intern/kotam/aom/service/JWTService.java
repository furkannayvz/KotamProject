package com.i2i.intern.kotam.aom.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long jwtRefreshExpiration;

    /*** PUBLIC TOKEN OPERATIONS ***/

    public String retrieveMsisdnFromToken(String token) {
        return extractTokenClaim(token, Claims::getSubject);
    }

    public <T> T extractTokenClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseAllTokenClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean validateTokenForUser(String token, UserDetails userDetails) {
        final String msisdn = retrieveMsisdnFromToken(token);
        return (msisdn.equals(userDetails.getUsername()) && !checkIfTokenExpired(token));
    }

    public String createAccessToken(UserDetails userDetails) {
        return createAccessToken(new HashMap<>(), userDetails);
    }

    public String createAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return constructToken(extraClaims, userDetails, jwtExpiration);
    }

    public String createRefreshToken(UserDetails userDetails) {
        return constructToken(new HashMap<>(), userDetails, jwtRefreshExpiration);
    }

    // ALIAS METHODS
    public String generateToken(UserDetails userDetails) {
        return createAccessToken(userDetails);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return createRefreshToken(userDetails);
    }

    /*** PRIVATE TOKEN UTILITIES ***/

    private Claims parseAllTokenClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getTokenSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String constructToken(Map<String, Object> extraClaims,
                                  UserDetails userDetails,
                                  long tokenExpiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // msisdn
                .claim("role", userDetails.getAuthorities().iterator().next().getAuthority())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getTokenSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean checkIfTokenExpired(String token) {
        return retrieveTokenExpiration(token).before(new Date());
    }

    private Date retrieveTokenExpiration(String token) {
        return extractTokenClaim(token, Claims::getExpiration);
    }

    private Key getTokenSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
