package com.mitocode.webflux.service;

import com.mitocode.webflux.pagination.PageSupport;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICRUD<T, ID> {

    Mono<T> save(T t);
    Mono<T> update(ID id, T t);
    Flux<T> findAll();
    Mono<T> findById(ID id);
    Mono<Boolean> delete(ID id);
    Mono<PageSupport<T>> getPage(Pageable pageable);
}
