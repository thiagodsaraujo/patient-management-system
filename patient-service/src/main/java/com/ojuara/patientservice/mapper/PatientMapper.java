package com.ojuara.patientservice.mapper;

import com.ojuara.patientservice.dto.PatientRequestDTO;
import com.ojuara.patientservice.dto.PatientResponseDTO;
import com.ojuara.patientservice.model.Patient;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient patient){
        PatientResponseDTO dto = new PatientResponseDTO();

        dto.setId(patient.getId().toString());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setAddress(patient.getAddress());
        dto.setDateOfBirth(patient.getDateOfBirth().toString());
        dto.setRegisteredDate(patient.getRegisteredDate().toString());

        return dto;
    }

    public static Patient toEntity(PatientRequestDTO dto){
        Patient patient = new Patient();

        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setAddress(dto.getAddress());
        patient.setDateOfBirth(java.time.LocalDate.parse(dto.getDateOfBirth()));
        patient.setRegisteredDate(java.time.LocalDate.parse(dto.getRegisteredDate()));

        return patient;
    }

}
