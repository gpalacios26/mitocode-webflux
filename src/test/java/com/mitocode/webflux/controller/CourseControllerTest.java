package com.mitocode.webflux.controller;

import com.mitocode.webflux.dto.CourseDTO;
import com.mitocode.webflux.model.Course;
import com.mitocode.webflux.service.ICourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ICourseService service;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private WebProperties.Resources resources;

    private Course course1;
    private Course course2;
    private CourseDTO course1DTO;
    private CourseDTO course2DTO;
    private List<Course> courses;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        course1 = new Course("1", "Lenguaje", "LEN", true);
        course2 = new Course("2", "Historia", "HIS", true);

        course1DTO = new CourseDTO("1", "Lenguaje", "LEN", true);
        course2DTO = new CourseDTO("2", "Historia", "HIS", true);

        courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void findAllTest(){
        Mockito.when(service.findAll()).thenReturn(Flux.fromIterable(courses));
        Mockito.when(modelMapper.map(course1, CourseDTO.class)).thenReturn(course1DTO);
        Mockito.when(modelMapper.map(course2, CourseDTO.class)).thenReturn(course2DTO);

        client.get()
                .uri("/courses")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void findByIdTest(){
        Mockito.when(service.findById("1")).thenReturn(Mono.just(course1));
        Mockito.when(modelMapper.map(course1, CourseDTO.class)).thenReturn(course1DTO);

        client.get()
                .uri("/courses/" + course1DTO.getIdCourse())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void saveTest(){
        Mockito.when(service.save(any())).thenReturn(Mono.just(course1));
        Mockito.when(modelMapper.map(course1, CourseDTO.class)).thenReturn(course1DTO);

        client.mutateWith(SecurityMockServerConfigurers.csrf()).post()
                .uri("/courses")
                .body(Mono.just(course1DTO), CourseDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void updateTest(){
        Mockito.when(modelMapper.map(course1DTO, Course.class)).thenReturn(course1);
        Mockito.when(service.update(any(), any())).thenReturn(Mono.just(course1));
        Mockito.when(modelMapper.map(course1, CourseDTO.class)).thenReturn(course1DTO);

        client.mutateWith(SecurityMockServerConfigurers.csrf()).put()
                .uri("/courses/" + course1DTO.getIdCourse())
                .body(Mono.just(course1DTO), CourseDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void deleteTest(){
        Mockito.when(service.delete(any())).thenReturn(Mono.just(true));

        client.mutateWith(SecurityMockServerConfigurers.csrf()).delete()
                .uri("/courses/" + course1DTO.getIdCourse())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void deleteTestFalse(){
        Mockito.when(service.delete(any())).thenReturn(Mono.just(false));

        client.mutateWith(SecurityMockServerConfigurers.csrf()).delete()
                .uri("/courses/" + course1DTO.getIdCourse())
                .exchange()
                .expectStatus().isNotFound();
    }

}