package com.mitocode.webflux.handler;

import com.mitocode.webflux.dto.StudentDTO;
import com.mitocode.webflux.model.Student;
import com.mitocode.webflux.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class StudentHandler {

    private final IStudentService service;

    private final ModelMapper modelMapper;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll().map(this::convertToDto), StudentDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");

        return service.findById(id)
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<StudentDTO> monoStudentDTO = request.bodyToMono(StudentDTO.class);

        return monoStudentDTO
                .flatMap(e -> service.save(convertToDocument(e)))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .created(URI.create(request.uri().toString().concat("/").concat(e.getIdStudent())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                );
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(StudentDTO.class)
                .map(e -> {
                    e.setIdStudent(id);
                    return e;
                })
                .flatMap(e -> service.update(id, convertToDocument(e)))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");

        return service.delete(id)
                .flatMap(result -> {
                    if(result){
                        return ServerResponse.noContent().build();
                    }else{
                        return ServerResponse.notFound().build();
                    }
                });
    }

    public Mono<ServerResponse> readAllStudentsOrderByAgeAsc(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getStudentsOrderByAgeAsc().map(this::convertToDto), StudentDTO.class);
    }

    public Mono<ServerResponse> readAllStudentsOrderByAgeDesc(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getStudentsOrderByAgeDesc().map(this::convertToDto), StudentDTO.class);
    }

    private StudentDTO convertToDto(Student model) {
        return modelMapper.map(model, StudentDTO.class);
    }

    private Student convertToDocument(StudentDTO dto){
        return modelMapper.map(dto, Student.class);
    }
}
