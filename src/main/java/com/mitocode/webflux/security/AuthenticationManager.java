package com.mitocode.webflux.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

//Clase 5
@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String user = jwtUtil.getUsernameFromToken(token);

        if(user != null && jwtUtil.validateToken(token)){
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            List<String> roles = claims.get("roles", List.class);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, roles.stream().map(SimpleGrantedAuthority::new).toList());

            return Mono.just(auth);
        }else{
            return Mono.error(new BadCredentialsException("Invalid token"));
        }
    }
}
