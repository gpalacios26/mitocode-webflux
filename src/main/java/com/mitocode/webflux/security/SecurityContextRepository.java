package com.mitocode.webflux.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//Clase S6
@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !(authHeader.startsWith("Bearer ") || authHeader.startsWith("bearer "))) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not Authenticated"));
        }

        final int TOKEN_POSITION = 1;

        String token = authHeader.split(" ")[TOKEN_POSITION];
        Authentication auth = new UsernamePasswordAuthenticationToken(token, token);

        return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
    }
}
