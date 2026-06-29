package com.api_gateway.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtilities {

    private final  String secretKey = "dewadaweghwejhgf3727y234983u2823982r2389eyiuc2hbfyuriucbwibcuyu2bru43yubfhb3jhbfds";


    public String generateToken(UserDetails userDetails){

        String role = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()),  SignatureAlgorithm.HS256)
                .compact();

    }

    public Claims getClaimsFromToken(String token){

            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getPayload();
            return claims;


    }

    public String getUsernameFromToken(String token){
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        }catch (Exception e){
            System.out.println("Invalid token" + e.getMessage());
            return "";
        }
    }

    public String extractRole(String token){
        Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    public boolean isTokenExpired(String token){
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration().before(new Date());

    }

    public boolean validateToken(String token, UserDetails userDetails){

        Claims claims = getClaimsFromToken(token);

        return claims.getSubject().equals(userDetails.getUsername()) && !isTokenExpired(token);

    }
}
