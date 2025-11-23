package com.ojuara.patientservice.dto.validators;

public interface CreatePatientValidationGroup {

    // Interface de marcação para validação de criação de paciente
    // Pode ser usada para agrupar validações específicas
    // Como por exemplo, validações que só se aplicam na criação
    // Exemplo: campos obrigatórios que não podem ser nulos ao criar um paciente

    // Exemplo de implementação futura:
    // @NotNull(groups = CreatePatientValidationGroup.class)
    // private String someField;

}
