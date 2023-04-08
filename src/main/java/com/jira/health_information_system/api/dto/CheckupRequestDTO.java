package com.jira.health_information_system.api.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CheckupRequestDTO {
    @NotBlank(message = "Invalid remark: Empty remark")
    @NotNull(message = "Invalid remark: remark is NULL")
    @Size(min = 3, max = 300, message = "Invalid remark: Must be of 3 - 300 characters")
    private String remarks;

    @Min(value = 0, message = "Invalid value: vital stats need to have a valid value")
    private int patientId, heartRate, respiratoryRate, sysBloodPressure, diaBloodPressure, oxygenSaturation;

    @PastOrPresent(message = "Invalid date of visit: must be past or present")
    private Date dateOfVisit;

    @Min(value = 0, message = "Invalid body temperature: Equals to zero or Less than zero")
    @Max(value = 100, message = "Invalid body temperature: Exceeds 100 degrees")
    private float bodyTemperature;
}
