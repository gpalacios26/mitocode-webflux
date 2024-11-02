package com.mitocode.webflux.controller;

import com.mitocode.webflux.dto.StudentDTO;
import com.mitocode.webflux.model.Student;
import com.mitocode.webflux.service.IStudentService;
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
@WebFluxTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private IStudentService service;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private WebProperties.Resources resources;

    private Student student1;
    private Student student2;
    private StudentDTO student1DTO;
    private StudentDTO student2DTO;
    private List<Student> students;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        student1 = new Student("1", "Gregory", "Palacios", "34234543", 32);
        student2 = new Student("2", "Pedro", "Perez", "87234983", 41);

        student1DTO = new StudentDTO("1", "Gregory", "Palacios", "34234543", 32);
        student2DTO = new StudentDTO("2", "Pedro", "Perez", "87234983", 41);

        students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void findAllTest(){
        Mockito.when(service.findAll()).thenReturn(Flux.fromIterable(students));
        Mockito.when(modelMapper.map(student1, StudentDTO.class)).thenReturn(student1DTO);
        Mockito.when(modelMapper.map(student2, StudentDTO.class)).thenReturn(student2DTO);

        client.get()
                .uri("/students")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void findByIdTest(){
        Mockito.when(service.findById("1")).thenReturn(Mono.just(student1));
        Mockito.when(modelMapper.map(student1, StudentDTO.class)).thenReturn(student1DTO);

        client.get()
                .uri("/students/" + student1DTO.getIdStudent())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void saveTest(){
        Mockito.when(service.save(any())).thenReturn(Mono.just(student1));
        Mockito.when(modelMapper.map(student1, StudentDTO.class)).thenReturn(student1DTO);

        client.mutateWith(SecurityMockServerConfigurers.csrf()).post()
                .uri("/students")
                .body(Mono.just(student1DTO), StudentDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void updateTest(){
        Mockito.when(modelMapper.map(student1DTO, Student.class)).thenReturn(student1);
        Mockito.when(service.update(any(), any())).thenReturn(Mono.just(student1));
        Mockito.when(modelMapper.map(student1, StudentDTO.class)).thenReturn(student1DTO);

        client.mutateWith(SecurityMockServerConfigurers.csrf()).put()
                .uri("/students/" + student1DTO.getIdStudent())
                .body(Mono.just(student1DTO), StudentDTO.class)
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
                .uri("/students/" + student1DTO.getIdStudent())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void deleteTestFalse(){
        Mockito.when(service.delete(any())).thenReturn(Mono.just(false));

        client.mutateWith(SecurityMockServerConfigurers.csrf()).delete()
                .uri("/students/" + student1DTO.getIdStudent())
                .exchange()
                .expectStatus().isNotFound();
    }

}