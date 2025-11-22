package com.ojuara.patientservice.controller;

import com.ojuara.patientservice.dto.PatientRequestDTO;
import com.ojuara.patientservice.dto.PatientResponseDTO;
import com.ojuara.patientservice.mapper.PatientMapper;
import com.ojuara.patientservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO) {

        PatientResponseDTO createdPatient = patientService.createPatient(patientRequestDTO);






        return ResponseEntity.ok().body(createdPatient);
    }

}
