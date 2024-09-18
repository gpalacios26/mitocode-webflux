package com.mitocode.webflux.service.impl;

import com.mitocode.webflux.model.Role;
import com.mitocode.webflux.model.User;
import com.mitocode.webflux.repository.IGenericRepository;
import com.mitocode.webflux.repository.IRoleRepository;
import com.mitocode.webflux.repository.IUserRepository;
import com.mitocode.webflux.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends CRUDImpl<User, String> implements IUserService {

    private final IUserRepository userRepo;

    private final IRoleRepository roleRepo;

    private final BCryptPasswordEncoder bcrypt;

    @Override
    protected IGenericRepository<User, String> getRepository() {
        return userRepo;
    }

    @Override
    public Mono<User> saveHash(User user) {
        user.setPassword(bcrypt.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Mono<com.mitocode.webflux.security.User> searchByUser(String username) {
        return userRepo.findOneByUsername(username)
                .flatMap(user -> Flux.fromIterable(user.getRoles())
                        .flatMap(userRole -> roleRepo.findById(userRole.getId())
                                .map(Role::getName))
                        .collectList()
                        .map(roles -> new com.mitocode.webflux.security.User(user.getUsername(), user.getPassword(), user.isStatus(), roles))
                );
    }


}
