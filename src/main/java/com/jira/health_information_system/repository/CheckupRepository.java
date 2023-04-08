package com.jira.health_information_system.repository;

import com.jira.health_information_system.api.model.Checkup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CheckupRepository {

    protected final JdbcTemplate jdbc;
    private final int pageSize = 20;

    @Autowired
    public CheckupRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void addCheckupEntry(Checkup checkup) throws DataAccessException {
        try {
            jdbc.execute("INSERT INTO checkup (patient_id, date_of_visit, body_temperature, heart_rate, " + "respiratory_rate, sys_blood_pressure, dia_blood_pressure, oxygen_saturation, remarks) " + "VALUES (?,?,?,?,?,?,?,?,?);", (PreparedStatementCallback<Boolean>) ps -> {
                ps.setInt(1, checkup.getPatientId());
                ps.setDate(2, new java.sql.Date(checkup.getDateOfVisit().getTime()));
                ps.setFloat(3, checkup.getBodyTemperature());
                ps.setInt(4, checkup.getHeartRate());
                ps.setInt(5, checkup.getRespiratoryRate());
                ps.setInt(6, checkup.getSysBloodPressure());
                ps.setInt(7, checkup.getDiaBloodPressure());
                ps.setInt(8, checkup.getOxygenSaturation());
                ps.setString(9, checkup.getRemarks());

                return ps.execute();
            });
        } catch (DataAccessException e) {
            throw e;
        }
    }

    public List<Checkup> getPatientCheckups(Integer patientId, Integer page) {
            List<Checkup> checkups = new ArrayList<>();
        try {
            var res = jdbc.queryForList("SELECT * FROM checkup WHERE patient_id = ? LIMIT ? OFFSET ?;", patientId, pageSize, (page - 1) * pageSize);
            res.forEach(stringObjectMap -> {
                int id = (int) stringObjectMap.get("id");
                int patientID = (int) stringObjectMap.get("patient_id");
                int heartRate = (int) stringObjectMap.get("heart_rate");
                int respiratoryRate = (int) stringObjectMap.get("respiratory_rate");
                int sysBloodPressure = (int) stringObjectMap.get("sys_blood_pressure");
                int diaBloodPressure = (int) stringObjectMap.get("dia_blood_pressure");
                int oxygenSaturation = (int) stringObjectMap.get("oxygen_saturation");
                Date dateOfVisit = new Date(((java.sql.Date) stringObjectMap.get("date_of_visit")).getTime());
                String remarks = (String) stringObjectMap.get("remarks");
                float bodyTemperature = (float) stringObjectMap.get("body_temperature");
                Checkup newCheckup = new Checkup(patientID, heartRate, respiratoryRate, sysBloodPressure,
                        diaBloodPressure, oxygenSaturation, dateOfVisit, bodyTemperature, remarks);
                newCheckup.setId(id);
                checkups.add(newCheckup);
            });
            return checkups;
        } catch (DataAccessException ex) {
            throw ex;
        }
    }
}
