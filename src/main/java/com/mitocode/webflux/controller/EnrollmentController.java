package com.mitocode.webflux.controller;

import com.mitocode.webflux.dto.EnrollmentDTO;
import com.mitocode.webflux.model.Enrollment;
import com.mitocode.webflux.service.IEnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/enrollment")
@RequiredArgsConstructor
@Slf4j
public class EnrollmentController {

    private final IEnrollmentService service;

    private final ModelMapper modelMapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<EnrollmentDTO>>> findAll() {
        Flux<EnrollmentDTO> fx = service.findAll().map(this::convertToDto);

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<EnrollmentDTO>> findById(@PathVariable("id") String id) {
        return service.findById(id)
                .map(this::convertToDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<EnrollmentDTO>> save(@Valid @RequestBody EnrollmentDTO dto, final ServerHttpRequest req) {
        return service.save(this.convertToDocument(dto))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.created(
                                        URI.create(req.getURI().toString().concat("/").concat(e.getIdEnrollment()))
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<EnrollmentDTO>> update(@Valid @PathVariable("id") String id, @RequestBody EnrollmentDTO dto) {
        return Mono.just(convertToDocument(dto))
                .map(e -> {
                    e.setIdEnrollment(id);
                    return e;
                })
                .flatMap(e -> service.update(id, e))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
        return service.delete(id)
                .flatMap(result -> {
                    if (result) {
                        return Mono.just(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    private EnrollmentDTO convertToDto(Enrollment model) {
        return modelMapper.map(model, EnrollmentDTO.class);
    }

    private Enrollment convertToDocument(EnrollmentDTO dto){
        return modelMapper.map(dto, Enrollment.class);
    }
}
