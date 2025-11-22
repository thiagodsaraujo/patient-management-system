package com.ojuara.patientservice.controller;

import com.ojuara.patientservice.dto.PatientResponseDTO;
import com.ojuara.patientservice.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients") //http://localhost:8080/api/v1/patients
public class PatientController {

    private final PatientService patientService;


    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>>getPatients() {
        List<PatientResponseDTO> patients = patientService.getAllPatients();

        return ResponseEntity.ok().body(patients);

    }

}
