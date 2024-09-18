package com.mitocode.webflux.controller;

import com.mitocode.webflux.model.User;
import com.mitocode.webflux.security.AuthRequest;
import com.mitocode.webflux.security.AuthResponse;
import com.mitocode.webflux.security.JwtUtil;
import com.mitocode.webflux.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    private final IUserService service;

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest authRequest){
        return service.searchByUser(authRequest.getUsername())
                .map(userDetails -> {
                    if(BCrypt.checkpw(authRequest.getPassword(), userDetails.getPassword())){
                        String token = jwtUtil.generateToken(userDetails);
                        Date expiration = jwtUtil.getExpirationDateFromToken(token);

                        return ResponseEntity.ok(new AuthResponse(token, expiration));
                    }else{
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<?>> register(@RequestBody AuthRequest authRequest){
        Mono<User> userRegister = service.saveHash(getDataUser(authRequest));
        return userRegister.flatMap(e -> service.searchByUser(e.getUsername())
               .map(userDetails -> {
                   if(BCrypt.checkpw(authRequest.getPassword(), userDetails.getPassword())){
                       String token = jwtUtil.generateToken(userDetails);
                       Date expiration = jwtUtil.getExpirationDateFromToken(token);

                       return ResponseEntity.ok(new AuthResponse(token, expiration));
                   }else{
                       return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                   }
               })
               .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    private User getDataUser(AuthRequest authRequest){
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(authRequest.getPassword());
        user.setStatus(true);
        user.setRoles(authRequest.getRoles());
        return user;
    }
}
