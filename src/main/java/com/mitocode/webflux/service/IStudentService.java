package com.mitocode.webflux.service;

import com.mitocode.webflux.model.Student;
import reactor.core.publisher.Flux;

public interface IStudentService extends ICRUD<Student, String> {

    Flux<Student> getStudentsOrderByAgeAsc();

    Flux<Student> getStudentsOrderByAgeDesc();
}
