package com.mitocode.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "enrollment")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Enrollment {

    @Id
    @EqualsAndHashCode.Include
    private String idEnrollment;

    @Field
    private Student student;

    @Field
    private LocalDateTime dateEnrollment;

    @Field
    private boolean status;

    @Field
    private List<EnrollmentDetail> details;
}
