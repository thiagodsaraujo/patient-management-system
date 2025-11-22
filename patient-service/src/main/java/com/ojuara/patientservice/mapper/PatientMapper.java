package com.ojuara.patientservice.mapper;

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



}
