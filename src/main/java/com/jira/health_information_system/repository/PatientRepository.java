package com.jira.health_information_system.repository;

import com.jira.health_information_system.api.model.AverageStats;
import com.jira.health_information_system.api.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PatientRepository {

    private static final RowMapper<Patient> itemMapper = new RowMapper<Patient>() {
        @Override
        public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String address = rs.getString("address");
            String phone = rs.getString("phone");
            int age = rs.getInt("age");
            Date dob = rs.getDate("dob");
            Patient.Gender gender = Patient.Gender.valueOf(rs.getString("gender"));
            Patient patient = new Patient(age, gender, name, address, phone, dob);
            patient.setId(id);
            return patient;
        }
    };

    private static final RowMapper<AverageStats> avgItemMapper = new RowMapper<AverageStats>() {
        @Override
        public AverageStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            int avgHeartRate = rs.getInt("avg_heart_rate");
            int avgRespiratoryRate = rs.getInt("avg_respiratory_rate");
            int avgSysBloodPressure = rs.getInt("avg_sys_blood_pressure");
            int avgDiaBloodPressure = rs.getInt("avg_dia_blood_pressure");
            int avgOxygenSaturation = rs.getInt("avg_oxygen_saturation");
            int avgBodyTemperature = rs.getInt("avg_body_temperature");
            return new AverageStats(avgHeartRate, avgRespiratoryRate, avgSysBloodPressure, avgDiaBloodPressure, avgOxygenSaturation, avgBodyTemperature);
        }
    };
    protected final JdbcTemplate jdbc;

    @Autowired
    public PatientRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void addNewPatient(Patient patient) throws DataAccessException {
        try {
            jdbc.execute("INSERT INTO patient (name, age, address, phone, gender, dob) VALUES (?,?,?,?,?,?)",
                    (PreparedStatementCallback<Boolean>) ps -> {
                        ps.setString(1, patient.getName());
                        ps.setInt(2, patient.getAge());
                        ps.setString(3, patient.getAddress());
                        ps.setString(4, patient.getPhone());
                        ps.setString(5, patient.getGender().name());
                        ps.setDate(6, new java.sql.Date(patient.getDob().getTime()));
                        return ps.execute();
                    });
        } catch (DataAccessException e) {
            throw e;
        }
    }

    public Iterable<Patient> getPatients(int page) {
        ArrayList<Patient> patients = new ArrayList<>();
        try {
            var res = jdbc.queryForList("SELECT * FROM patient LIMIT ? OFFSET ?;", 20, (page - 1) * 20);
            res.forEach(p -> {
                patients.add(extractPatient(p));
            });
            return patients;
        } catch (DataAccessException e) {
            return patients;
        }
    }

    public Optional getPatientByID(int id) {
        Optional optional = Optional.empty();
        try {
            var res = jdbc.queryForObject("SELECT * FROM patient WHERE id = ?;", itemMapper, id);
            if (res != null) {
                optional = Optional.of(res);
            }
            return optional;
        } catch (DataAccessException e) {
            return optional;
        }
    }

    public List<Patient> searchPatients(String searchTerm) {
        List<Patient> patients = new ArrayList<>();
        try {
            var res = jdbc.queryForList("SELECT * FROM patient WHERE name LIKE CONCAT(?,'%');", searchTerm);
            res.forEach(objectMap -> {
                patients.add(extractPatient(objectMap));
            });
            return patients;
        } catch (DataAccessException e) {
            throw e;
        }
    }

    public AverageStats getPatientAvgStats(Integer patientId) throws DataAccessException {
        try {
            var res = jdbc.queryForObject("SELECT AVG(heart_rate) as avg_heart_rate, AVG(respiratory_rate) AS avg_respiratory_rate, AVG(sys_blood_pressure) AS avg_sys_blood_pressure," +
                    "AVG(dia_blood_pressure) AS avg_dia_blood_pressure, AVG(oxygen_saturation) AS avg_oxygen_saturation, AVG(body_temperature) as avg_body_temperature" +
                    "FROM checkup WHERE patient_id = ?;", avgItemMapper, patientId);
            return res;
        } catch (DataAccessException ex) {
            throw ex;
        }
    }

    private Patient extractPatient(Map<String, Object> objectMap) {
        int id = (int) objectMap.get("id");
        String name = (String) objectMap.get("name");
        Patient.Gender gender = Patient.Gender.valueOf((String) objectMap.get("gender"));
        int age = (int) objectMap.get("age");
        String address = (String) objectMap.get("address");
        String phone = (String) objectMap.get("phone");
        java.util.Date dob = new java.util.Date(((java.sql.Date) objectMap.get("dob")).getTime());
        Patient patient = new Patient(age, gender, name, address, phone, dob);
        patient.setId(id);
        return patient;
    }
}
