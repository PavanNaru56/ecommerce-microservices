package com.api_gateway.api_gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements GlobalFilter {

    private UserDetails userDetails;

    private JwtUtilities jwtUtilities;
    public JwtFilter(JwtUtilities jwtUtilities) {
        this.jwtUtilities = jwtUtilities;
    }

    @Override
    public  Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){

        String path = exchange.getRequest().getURI().getPath();

        if(path.equals("/api/users/register") || path.equals("/api/users/login")){
            return chain.filter(exchange);
        }

        // get the authorization header to validate the token

        String header = exchange.getRequest().getHeaders().getFirst("Authorization");

        if(header == null || !header.startsWith("Bearer ")){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = header.substring(7);

//        try {
//            jwtUtilities.isTokenExpired(token);
//        }
//        catch (Exception e){
//            exchange.getResponse().setComplete();
//        }

        if(jwtUtilities.getUsernameFromToken(token).isEmpty()){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);



    }



}
