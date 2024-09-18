package com.mitocode.webflux.security;

import java.util.Date;

//Clase S3
public record AuthResponse(
        String token,
        Date expiration
) {
}
