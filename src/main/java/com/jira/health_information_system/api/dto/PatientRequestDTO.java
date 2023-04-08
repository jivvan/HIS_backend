package com.jira.health_information_system.api.dto;

import com.jira.health_information_system.api.model.Patient;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Data
@Builder
public class PatientRequestDTO {
    @Min(value=0,message = "Invalid age: less than 0")
    private int age;

    @NotNull
    private Patient.Gender gender;

    @Size(min = 3, max = 30, message = "Invalid name: must have 3 - 30 characters")
    private String name;
    @Size(min = 3, max = 30, message = "Invalid address: must have 3 - 30 characters")
    private String address;
    @Size(min=10, max=10, message = "Invalid phone: must have 10 characters")
    private String phone;
    @Past(message = "Invalid dob: must be a past date")
    private Date dob;
}
