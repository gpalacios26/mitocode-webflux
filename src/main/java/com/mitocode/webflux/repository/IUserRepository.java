package com.mitocode.webflux.repository;

import com.mitocode.webflux.model.User;
import reactor.core.publisher.Mono;

public interface IUserRepository extends IGenericRepository<User, String>{

    Mono<User> findOneByUsername(String username);
}
