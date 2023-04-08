package com.jira.health_information_system.api.controller;

import com.jira.health_information_system.api.dto.CheckupRequestDTO;
import com.jira.health_information_system.api.model.Checkup;
import com.jira.health_information_system.config.Helper;
import com.jira.health_information_system.repository.CheckupRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/checkups")
public class CheckupController {
    private final CheckupRepository checkupRepository;

    @Autowired
    public CheckupController(CheckupRepository checkupRepository) {
        this.checkupRepository = checkupRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addCheckupEntry(@RequestBody @Valid CheckupRequestDTO checkupRequest) {
        try {
            this.checkupRepository.addCheckupEntry(new Checkup(checkupRequest.getPatientId(),
                    checkupRequest.getHeartRate(), checkupRequest.getRespiratoryRate(), checkupRequest.getSysBloodPressure(),
                    checkupRequest.getDiaBloodPressure(), checkupRequest.getOxygenSaturation(), checkupRequest.getDateOfVisit(),
                    checkupRequest.getBodyTemperature(), checkupRequest.getRemarks()));
            return new ResponseEntity<>(Helper.makeMessage("created checkup entry successfully"), HttpStatus.CREATED);
        } catch (DataAccessException e) {
            if (e.getMessage().contains("a foreign key constraint fails")) {
                return new ResponseEntity<>(Helper.makeMessage("patient with patient_id does not exist"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(Helper.makeMessage("unable to create checkup entry"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{patient_id}")
    public ResponseEntity<List<Checkup>> getPatientCheckups(@PathVariable("patient_id") Integer patient_id, @RequestParam(name = "page", defaultValue = "1") Integer page) {
        try {
            return new ResponseEntity<>(this.checkupRepository.getPatientCheckups(patient_id, page), HttpStatus.OK);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
