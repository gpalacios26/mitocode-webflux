package com.mitocode.webflux.service.impl;

import com.mitocode.webflux.model.Enrollment;
import com.mitocode.webflux.repository.IEnrollmentRepository;
import com.mitocode.webflux.repository.IGenericRepository;
import com.mitocode.webflux.service.IEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl extends CRUDImpl<Enrollment, String> implements IEnrollmentService {

    private final IEnrollmentRepository repository;

    @Override
    protected IGenericRepository<Enrollment, String> getRepository() {
        return repository;
    }
}
