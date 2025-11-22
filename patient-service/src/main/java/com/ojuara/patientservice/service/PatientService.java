package com.ojuara.patientservice.service;

import com.ojuara.patientservice.dto.PatientResponseDTO;
import com.ojuara.patientservice.model.Patient;
import com.ojuara.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {


    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

}
