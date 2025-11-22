package com.ojuara.patientservice.service;

import com.ojuara.patientservice.dto.PatientRequestDTO;
import com.ojuara.patientservice.dto.PatientResponseDTO;
import com.ojuara.patientservice.exception.EmailAlreadyExistsException;
import com.ojuara.patientservice.mapper.PatientMapper;
import com.ojuara.patientservice.model.Patient;
import com.ojuara.patientservice.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {


    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public boolean patientExists(String email) {
        return patientRepository.existsByEmail(email);
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toDTO).toList();

    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {

        // Busca paciente existente por email
        Optional<Patient> patient = patientRepository.findByEmail(patientRequestDTO.getEmail());

        if (patient.isPresent()) {
            return  PatientMapper.toDTO(patient.get());
        }

//        // Cria novo paciente apenas se não existir (código nunca vai ser chamado)
//        Pois se acima for true, já retorna o paciente existente
//        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
//            throw new EmailAlreadyExistsException(
//                    "Patient with email " + patientRequestDTO.getEmail() + " already exists.");
//        }

        Patient newPatient = patientRepository.save(PatientMapper.toEntity(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);

    }





}
