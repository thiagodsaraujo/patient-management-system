package com.ojuara.patientservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequestDTO {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name can have at most 100 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Size(max = 50, message = "Email can have at most 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address is mandatory")
    @Size(max = 200, message = "Address can have at most 200 characters")
    private String address;

    @NotBlank(message = "Date of Birth is mandatory")
    private String dateOfBirth;

    @NotBlank(message = "Registered Date is mandatory")
    private String registeredDate;


}
