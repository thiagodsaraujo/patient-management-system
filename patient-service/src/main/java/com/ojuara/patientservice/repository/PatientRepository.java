package com.ojuara.patientservice.repository;

import com.ojuara.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsByEmail(String email);

    Optional<Patient> findByEmail(String email);

    /**
     * Verifica se já existe outro paciente com o mesmo email, excluindo o paciente
     * identificado por `id`. Usado para validar unicidade do email em operações de
     * atualização (garante que nenhum outro registro possua o email informado).
     *
     * @param email email a verificar
     * @param id id do paciente que deve ser ignorado na verificação
     * @return `true` se existir outro paciente com o mesmo email, `false` caso contrário
     */
    boolean existsByEmailAndIdNot(String email, UUID id);


}
