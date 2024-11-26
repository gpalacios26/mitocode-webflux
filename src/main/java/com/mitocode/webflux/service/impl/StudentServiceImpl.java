package com.mitocode.webflux.service.impl;

import com.mitocode.webflux.model.Student;
import com.mitocode.webflux.repository.IGenericRepository;
import com.mitocode.webflux.repository.IStudentRepository;
import com.mitocode.webflux.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl extends CRUDImpl<Student, String> implements IStudentService {

    private final IStudentRepository repository;

    @Override
    protected IGenericRepository<Student, String> getRepository() {
        return repository;
    }

    @Override
    public Flux<Student> getStudentsOrderByAgeAsc() {
        return repository.findAll().sort(Comparator.comparing(Student::getAge));
    }

    @Override
    public Flux<Student> getStudentsOrderByAgeDesc() {
        return repository.findAll().sort(Comparator.comparing(Student::getAge).reversed());
    }
}
