package com.mitocode.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentDetail {

    @Field
    private Course course;

    @Field
    private String classroom;
}
