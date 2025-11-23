package com.ojuara.patientservice.service;

import com.ojuara.patientservice.dto.PatientRequestDTO;
import com.ojuara.patientservice.dto.PatientResponseDTO;
import com.ojuara.patientservice.exception.EmailAlreadyExistsException;
import com.ojuara.patientservice.exception.PatientNotFoundException;
import com.ojuara.patientservice.mapper.PatientMapper;
import com.ojuara.patientservice.model.Patient;
import com.ojuara.patientservice.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

/**
 * Cria um novo paciente ou retorna o existente quando o email já está cadastrado.
 *
 * @param patientRequestDTO DTO contendo os dados do paciente a ser criado
 * @return PatientResponseDTO com os dados do paciente criado ou do paciente existente
 */
public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
    // Extrai o email do DTO e tenta localizar um paciente existente com esse email
    Optional<Patient> patient = patientRepository.findByEmail(patientRequestDTO.getEmail());

    // Se um paciente com o mesmo email já existir, converte a entidade encontrada para DTO e retorna
    if (patient.isPresent()) {
        return PatientMapper.toDTO(patient.get());
    }

    // Converte o DTO de requisição para a entidade Patient (preparando para persistir)
    Patient patientEntity = PatientMapper.toEntity(patientRequestDTO);

    // Persiste a nova entidade no repositório (retorna a entidade salva com id/valores gerados)
    Patient newPatient = patientRepository.save(patientEntity);

    // Converte a entidade persistida para DTO de resposta e retorna ao chamador
    return PatientMapper.toDTO(newPatient);
}

    /**
     * Atualiza um paciente existente.
     *
     * @param id UUID do paciente a atualizar
     * @param patientRequestDTO dados enviados para atualização
     * @return PatientResponseDTO com os dados atualizados
     * @throws PatientNotFoundException se nenhum paciente for encontrado para o id
     * @throws EmailAlreadyExistsException se o email informado já estiver em uso por outro paciente
     */
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {

        // Busca paciente por id; se não existir lança PatientNotFoundException
        Patient existingPatient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient with id " + id + " not found."));

        // Verifica se o email informado pertence a outro paciente (mesmo email, id diferente)
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            // Impede atualização quando o email já está em uso por outro registro
            throw new EmailAlreadyExistsException(
                    "Patient with email " + patientRequestDTO.getEmail() + " already exists.");
        }

        // Atualiza o nome do paciente com o valor vindo no DTO
        existingPatient.setName(patientRequestDTO.getName());
        // Atualiza o email do paciente com o valor vindo no DTO
        existingPatient.setEmail(patientRequestDTO.getEmail());
        // Atualiza o endereço do paciente com o valor vindo no DTO
        existingPatient.setAddress(patientRequestDTO.getAddress());
        // Converte a data (string) do DTO para LocalDate e seta no paciente
        existingPatient.setDateOfBirth(java.time.LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        // Persiste as alterações no repositório e recebe a entidade atualizada
        Patient updatedPatient = patientRepository.save(existingPatient);

        // Converte a entidade atualizada para DTO de resposta e retorna
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatientById(UUID id) {
        patientRepository.deleteById(id);
    }

}
