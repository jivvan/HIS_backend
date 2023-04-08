package com.jira.health_information_system.api.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AverageStats{
    public int avgHeartRate,avgRespiratoryRate,avgSysBloodPressure,avgDiaBloodPressure,avgOxygenSaturation;
    public float avgBodyTemperature;
}