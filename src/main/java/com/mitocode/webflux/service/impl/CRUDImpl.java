package com.mitocode.webflux.service.impl;

import com.mitocode.webflux.pagination.PageSupport;
import com.mitocode.webflux.repository.IGenericRepository;
import com.mitocode.webflux.service.ICRUD;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class CRUDImpl<T, ID> implements ICRUD<T, ID> {

    protected abstract IGenericRepository<T, ID> getRepository();

    @Override
    public Mono<T> save(T t) {
        return getRepository().save(t);
    }

    @Override
    public Mono<T> update(ID id, T t) {
        return getRepository().findById(id).flatMap(e -> getRepository().save(t));
    }

    @Override
    public Flux<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public Mono<T> findById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public Mono<Boolean> delete(ID id) {
        return getRepository().findById(id)
                .hasElement()
                .flatMap(result -> {
                    if(result){
                        return getRepository().deleteById(id).thenReturn(true);
                    }else{
                        return Mono.just(false);
                    }
                });
    }

    @Override
    public Mono<PageSupport<T>> getPage(Pageable pageable) {
        return getRepository().findAll()
                .collectList()
                .map(list -> new PageSupport<>(
                        //1,2,3,4,5,6,7,8,9,10
                        //pageNumber = 1
                        //pageSize = 2
                   list.stream()
                           .skip(pageable.getPageNumber() * pageable.getPageSize())
                           .limit(pageable.getPageSize()).toList()
                        ,
                   pageable.getPageNumber(),
                   pageable.getPageSize(),
                   list.size()
                ));
    }
}
