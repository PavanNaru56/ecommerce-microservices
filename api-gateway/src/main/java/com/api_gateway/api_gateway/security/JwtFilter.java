package com.api_gateway.api_gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Component
public class JwtFilter implements GlobalFilter {

    private UserDetails userDetails;

    private JwtUtilities jwtUtilities;
    public JwtFilter(JwtUtilities jwtUtilities) {
        this.jwtUtilities = jwtUtilities;
    }

    // Here we extract the token from the request and we will validate the token using the JwtUtilities
    // While using the microservices we will not store the token in SecurityContextHolder
    // For every request we will get the token from the exchange headers and validates the token and routes to the other services using the GatewayFilterChain
    // chain.filter(exchange)



    @Override
    public  Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){

        String path = exchange.getRequest().getURI().getPath();

        if(path.equals("/api/users/register") || path.equals("/api/users/login")){
            return chain.filter(exchange);
        }

        // get the authorization header to validate the token

        String header = exchange.getRequest().getHeaders().getFirst("Authorization");
   //     System.out.println("User Details in api-gateway : " + userDetails.getUsername());


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

        String role = jwtUtilities.extractRole(token);
        System.out.println("Role at gateway" + role);

        String username = jwtUtilities.getUsernameFromToken(token);
        System.out.println("Username at gateway " + username);

        if((path.equals("/api/users/list") || path.equals("/api/products/add") || path.equals("/api/products/delete/**") || path.equals("/api/products/update/**")) && !"ROLE_ADMIN".equals(role) ){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if(path.startsWith("/api/orders")){
            if(!role.equals("ROLE_ADMIN") && !role.equals("ROLE_USER")){
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }

        //mutate the header and pass the modified exchange
        // Context Propagation
        //get the request, add the username and role to the request and this modified exchange
        //exchanges are immutable, so we get the request and modify that send the modified exchange

        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("X-User-Username", username)
                .header("X-User-Role", role)
                .build();

        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(request).build();








        return chain.filter(modifiedExchange);



    }



}
