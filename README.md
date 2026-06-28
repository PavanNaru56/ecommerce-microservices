For Admin

Create a enum with ROLE_ADMIN, ROLE_USER
Change it in the CustomUserDetailsService class and return the proper roleb -> UserDetails
Chnage it in the JWTUtility class and return the role by UserDetails and add to it
Change it in the SecurityConfig file and assign requestMatchers().hasRole("ADMIN)