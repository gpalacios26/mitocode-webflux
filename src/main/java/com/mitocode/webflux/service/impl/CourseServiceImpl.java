package com.mitocode.webflux.service.impl;

import com.mitocode.webflux.model.Course;
import com.mitocode.webflux.repository.ICourseRepository;
import com.mitocode.webflux.repository.IGenericRepository;
import com.mitocode.webflux.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends CRUDImpl<Course, String> implements ICourseService {

    private final ICourseRepository repository;

    @Override
    protected IGenericRepository<Course, String> getRepository() {
        return repository;
    }
}
