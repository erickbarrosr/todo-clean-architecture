package com.ersoftwares.todocleanarchitecture.adapter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para criação de tarefa.
 *
 * @Data do Lombok gera: getters, setters, equals, hashCode e toString.
 * <p>
 * As anotações de validação (@NotBlank, @Size) são processadas pelo
 * Spring quando o controller usa @Valid — antes de chegar no use case.
 * Isso separa a validação de formato (DTO) da validação de negócio (domain).
 * <p>
 * Por que ter validação no DTO E na entidade?
 * - DTO: valida formato da entrada HTTP (campo obrigatório, tamanho máximo)
 * - Task: valida regra de negócio (título não pode ser vazio no domínio)
 * São camadas diferentes de proteção.
 */
@Data
public class CreateTaskRequest {

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
    private String title;
}
