package com.jira.health_information_system.api.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
public class Checkup {
    private int id, patientId, heartRate, respiratoryRate, sysBloodPressure, diaBloodPressure, oxygenSaturation;
    private Date dateOfVisit;
    private float bodyTemperature;
    private String remarks;

    public Checkup(int patientId, int heartRate, int respiratoryRate, int sysBloodPressure, int diaBloodPressure,
                   int oxygenSaturation, Date dateOfVisit, float bodyTemperature, String remarks
                   ){
        this.patientId = patientId;
        this.heartRate = heartRate;
        this.respiratoryRate = respiratoryRate;
        this.sysBloodPressure = sysBloodPressure;
        this.diaBloodPressure = diaBloodPressure;
        this.oxygenSaturation = oxygenSaturation;
        this.dateOfVisit = dateOfVisit;
        this.bodyTemperature = bodyTemperature;
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Checkup{" +
                "id=" + id +
                ", patient_id=" + patientId +
                ", heart_rate=" + heartRate +
                ", respiratory_rate=" + respiratoryRate +
                ", sys_blood_pressure=" + sysBloodPressure +
                ", dia_blood_pressure=" + diaBloodPressure +
                ", oxygen_saturation=" + oxygenSaturation +
                ", date_of_visit=" + dateOfVisit +
                ", body_temperature=" + bodyTemperature +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}

