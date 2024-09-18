package com.mitocode.webflux.service;

import com.mitocode.webflux.model.User;
import reactor.core.publisher.Mono;

public interface IUserService extends ICRUD<User, String>{

    Mono<User> saveHash(User user);
    Mono<com.mitocode.webflux.security.User> searchByUser(String username);
}
