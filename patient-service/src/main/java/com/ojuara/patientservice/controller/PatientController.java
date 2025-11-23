package com.ojuara.patientservice.controller;

import com.ojuara.patientservice.dto.PatientRequestDTO;
import com.ojuara.patientservice.dto.PatientResponseDTO;
import com.ojuara.patientservice.dto.validators.CreatePatientValidationGroup;
import com.ojuara.patientservice.mapper.PatientMapper;
import com.ojuara.patientservice.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    /**
     * Cria um paciente de forma idempotente.
     *
     * <p>Fluxo:
     * - Valida o DTO usando os grupos Default e CreatePatientValidationGroup.
     * - Verifica se já existe um paciente com o mesmo e-mail (para decidir o status HTTP).
     * - Solicita ao service que crie ou retorne o paciente correspondente.
     * - Retorna 201 CREATED se foi realmente criado, ou 200 OK se já existia.</p>
     *
     * <p>Observação: a checagem de existência é feita aqui apenas para escolher o código HTTP.
     * A responsabilidade de garantir inexistência de duplicatas e tratar condições de corrida
     * deve ficar implementada em `PatientService.createPatient`.</p>
     *
     * @param patientRequestDTO DTO com os dados do paciente (payload da requisição)
     * @return ResponseEntity contendo o paciente criado ou o existente e o status apropriado
     */
    @PostMapping("/create")
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDTO patientRequestDTO) {

        // 1) Verifica se já existe um paciente com o e-mail informado.
        //    Isso é usado somente para definir o status HTTP de retorno (OK vs CREATED).
        boolean exists = patientService.patientExists(patientRequestDTO.getEmail());

        // 2) Delega ao service a criação ou recuperação do paciente.
        //    O service deve garantir idempotência real (não criar duplicatas).
        PatientResponseDTO createdPatient = patientService.createPatient(patientRequestDTO);

        // 3) Escolhe o status HTTP: 201 se criou, 200 se já existia.
        HttpStatus status = exists ? HttpStatus.OK : HttpStatus.CREATED;

        // 4) Retorna o paciente e o status apropriado.
        return ResponseEntity.status(status).body(createdPatient);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable("id") UUID id,
            @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {

        PatientResponseDTO updatedPatient = patientService.updatePatient(id, patientRequestDTO);

        return ResponseEntity.ok().body(updatedPatient);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") UUID id) {
        patientService.deletePatientById(id);
        return ResponseEntity.noContent().build();
    }


}
