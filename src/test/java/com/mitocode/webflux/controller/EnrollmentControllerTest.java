package com.mitocode.webflux.controller;

import com.mitocode.webflux.dto.CourseDTO;
import com.mitocode.webflux.dto.EnrollmentDTO;
import com.mitocode.webflux.dto.EnrollmentDetailDTO;
import com.mitocode.webflux.dto.StudentDTO;
import com.mitocode.webflux.model.Course;
import com.mitocode.webflux.model.Enrollment;
import com.mitocode.webflux.model.EnrollmentDetail;
import com.mitocode.webflux.model.Student;
import com.mitocode.webflux.service.IEnrollmentService;
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
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private IEnrollmentService service;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private WebProperties.Resources resources;

    private Enrollment enrollment1;
    private Enrollment enrollment2;
    private EnrollmentDTO enrollment1DTO;
    private EnrollmentDTO enrollment2DTO;
    private List<Enrollment> enrollments;
    private final Student student = new Student("1", "Gregory", "Palacios", "34234543", 32);
    private final StudentDTO studentDTO = new StudentDTO("1", "Gregory", "Palacios", "34234543", 32);
    private final LocalDateTime dateEnrollment = LocalDateTime.parse("2024-02-27T18:14:01.184");

    private final Course course = new Course("1", "Lenguaje", "LEN", true);
    private final EnrollmentDetail detail = new EnrollmentDetail(course, "A");
    private final List<EnrollmentDetail> details = new ArrayList<>();

    private final CourseDTO courseDTO = new CourseDTO("1", "Lenguaje", "LEN", true);
    private final EnrollmentDetailDTO detailDTO = new EnrollmentDetailDTO(courseDTO, "A");
    private final List<EnrollmentDetailDTO> detailsDTO = new ArrayList<>();

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        details.add(detail);
        detailsDTO.add(detailDTO);

        enrollment1 = new Enrollment("1", student, dateEnrollment, true, details);
        enrollment2 = new Enrollment("2", student, dateEnrollment, true, details);

        enrollment1DTO = new EnrollmentDTO("1", studentDTO, dateEnrollment, true, detailsDTO);
        enrollment2DTO = new EnrollmentDTO("2", studentDTO, dateEnrollment, true, detailsDTO);

        enrollments = new ArrayList<>();
        enrollments.add(enrollment1);
        enrollments.add(enrollment2);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void findAllTest(){
        Mockito.when(service.findAll()).thenReturn(Flux.fromIterable(enrollments));
        Mockito.when(modelMapper.map(enrollment1, EnrollmentDTO.class)).thenReturn(enrollment1DTO);
        Mockito.when(modelMapper.map(enrollment2, EnrollmentDTO.class)).thenReturn(enrollment2DTO);

        client.get()
                .uri("/enrollment")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void findByIdTest(){
        Mockito.when(service.findById("1")).thenReturn(Mono.just(enrollment1));
        Mockito.when(modelMapper.map(enrollment1, EnrollmentDTO.class)).thenReturn(enrollment1DTO);

        client.get()
                .uri("/enrollment/" + enrollment1DTO.getIdEnrollment())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void saveTest(){
        Mockito.when(service.save(any())).thenReturn(Mono.just(enrollment1));
        Mockito.when(modelMapper.map(enrollment1, EnrollmentDTO.class)).thenReturn(enrollment1DTO);

        client.mutateWith(SecurityMockServerConfigurers.csrf()).post()
                .uri("/enrollment")
                .body(BodyInserters.fromValue(enrollment1))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void updateTest(){
        Mockito.when(modelMapper.map(enrollment1DTO, Enrollment.class)).thenReturn(enrollment1);
        Mockito.when(service.update(any(), any())).thenReturn(Mono.just(enrollment1));
        Mockito.when(modelMapper.map(enrollment1, EnrollmentDTO.class)).thenReturn(enrollment1DTO);

        client.mutateWith(SecurityMockServerConfigurers.csrf()).put()
                .uri("/enrollment/" + enrollment1DTO.getIdEnrollment())
                .body(BodyInserters.fromValue(enrollment1))
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
                .uri("/enrollment/" + enrollment1DTO.getIdEnrollment())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void deleteTestFalse(){
        Mockito.when(service.delete(any())).thenReturn(Mono.just(false));

        client.mutateWith(SecurityMockServerConfigurers.csrf()).delete()
                .uri("/enrollment/" + enrollment1DTO.getIdEnrollment())
                .exchange()
                .expectStatus().isNotFound();
    }

}