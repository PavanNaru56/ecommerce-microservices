package com.user.service.user_service.security;


import com.user.service.user_service.service.CustomUserDetails;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    private final CustomUserDetails userDetailsService;

    JwtAuthFilter(JwtUtility jwtUtility, CustomUserDetails userDetailsService){
        this.jwtUtility = jwtUtility;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException{

        String path = request.getRequestURI();

        if(path.equals("/api/users/register") || path.equals("/api/users/login")){
            filterChain.doFilter(request,response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")){

            try{
                String jwt = authHeader.substring(7);

                String username = jwtUtility.getUsernameFromToken(jwt);

                // if the securityContextHolder is null then store the authenticationToken in securityContextHolder

                if(username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);



                    if(jwtUtility.validateToken(jwt, userDetails)){

//                        String role = jwtUtility.extractRole(jwt);
//                        System.out.println("Role from JWT = " + role);
//                        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(role));
                        UsernamePasswordAuthenticationToken authToken  = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        // this for storing the IP address and other compnents
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);



                    }

                }
            }catch (Exception e){
                System.out.println("Jwt auth failed" + e.getMessage());
            }
        }
       filterChain.doFilter(request, response);
    }
}
