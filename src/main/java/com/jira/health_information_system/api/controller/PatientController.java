package com.jira.health_information_system.api.controller;

import com.jira.health_information_system.api.dto.PatientRequestDTO;
import com.jira.health_information_system.api.model.AverageStats;
import com.jira.health_information_system.api.model.Patient;
import com.jira.health_information_system.config.Helper;
import com.jira.health_information_system.repository.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/patients")
public class PatientController {

    private PatientRepository patientRepository;

    @Autowired
    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addNewPatient(@RequestBody @Valid PatientRequestDTO patientRequest) {
        try {
            this.patientRepository.addNewPatient(new Patient(patientRequest.getAge(), patientRequest.getGender(),
                    patientRequest.getName(), patientRequest.getAddress(), patientRequest.getPhone(),
                    patientRequest.getDob()));
            return new ResponseEntity<>(Helper.makeMessage("created new patient successfully"), HttpStatus.CREATED);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>(Helper.makeMessage("failed to create new patient"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public @ResponseBody Iterable<Patient> getPatients(@RequestParam(name = "page", required = false, defaultValue = "1") String page) {
        int pageNo = Integer.parseInt(page);
        if (pageNo <= 0) {
            pageNo = 1;
        }
        return this.patientRepository.getPatients(pageNo);
    }

    @GetMapping("/search/{search_term}") ResponseEntity<List<Patient>> searchPatients(@PathVariable("search_term")String search_term){
        if(search_term==""){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
        try{
            return new ResponseEntity<>(this.patientRepository.searchPatients(search_term), HttpStatus.OK);
        }catch (DataAccessException e){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{patient_id}")
    public @ResponseBody Optional<Patient> getPatientByID(@PathVariable("patient_id") Integer patient_id) {
        return this.patientRepository.getPatientByID(patient_id);
    }

    @GetMapping("/average/{patient_id}")
    public ResponseEntity<AverageStats>getPatientAvgStats(@PathVariable("patient_id")Integer patient_id){
        try{
            return new ResponseEntity<>(this.patientRepository.getPatientAvgStats(patient_id), HttpStatus.OK);
        }catch (DataAccessException ex){
            System.out.println(ex.getMessage());
            return  new ResponseEntity<>(new AverageStats(0,0,0,0,0,0), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
