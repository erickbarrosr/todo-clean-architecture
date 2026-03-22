package com.ersoftwares.todocleanarchitecture.presentation.dto;

import com.ersoftwares.todocleanarchitecture.domain.model.Task;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de saída — representa uma tarefa na resposta HTTP.
 * <p>
 * Por que não retornar a Task diretamente?
 * 1. Controle de exposição: podemos omitir campos internos (auditoria, etc.)
 * 2. Versionamento de API: a estrutura do JSON pode mudar sem afetar o domínio
 * 3. Serialização: Jackson serializa facilmente, sem surpresas de lazy loading JPA
 * <p>
 * O método estático 'from' é um factory method —
 * centraliza a conversão Task → TaskResponse em um único lugar.
 */
@Data
public class TaskResponse {

    private Long id;
    private String title;
    private boolean completed;
    private LocalDateTime createdAt;

    /**
     * Converte uma entidade de domínio para DTO de resposta.
     * Factory method estático: leitura fluente — TaskResponse.from(task).
     */
    public static TaskResponse from(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setCompleted(task.isCompleted());
        response.setCreatedAt(task.getCreatedAt());
        return response;
    }
}
