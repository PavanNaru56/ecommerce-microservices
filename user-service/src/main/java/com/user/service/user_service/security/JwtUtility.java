package com.user.service.user_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtility {

    private final String secretKey = "dewadaweghwejhgf3727y234983u2823982r2389eyiuc2hbfyuriucbwibcuyu2bru43yubfhb3jhbfds";



    public String generateToken(UserDetails userDetails){

        // for admin creation get the role

        String role = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();

        System.out.println("User Details in user-service : " + userDetails.getUsername());
        System.out.println("Role : " + role);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()),  SignatureAlgorithm.HS256)
                .compact();

    }

    public Claims getClaimsFromToken(String token){

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getPayload();

    }

    public String getUsernameFromToken(String token){

        Claims claims = getClaimsFromToken(token);
        System.out.println(claims.getSubject());
        return claims.getSubject();
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
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


}
