package com.mitocode.webflux.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDTO {

    private String idCourse;

    @NotNull
    @Size(min = 3, max = 25)
    private String name;

    @NotNull
    private String acronym;

    @NotNull
    private boolean status;
}
