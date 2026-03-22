package com.ersoftwares.todocleanarchitecture.application.usecase;

import com.ersoftwares.todocleanarchitecture.domain.model.Task;
import com.ersoftwares.todocleanarchitecture.domain.port.TaskRepository;
import org.springframework.stereotype.Service;

/**
 * Use Case: marcar uma tarefa como concluída.
 * <p>
 * Observe a separação de responsabilidades:
 * - Este use case orquestra: busca, delega comportamento, persiste.
 * - A validação de negócio (já está completa?) fica na entidade Task.
 * - O "não encontrado" é uma regra de aplicação — fica aqui.
 */
@Service
public class CompleteTaskUseCase {

    private final TaskRepository taskRepository;

    public CompleteTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * @param taskId ID da tarefa a completar
     * @return a tarefa atualizada
     * @throws IllegalArgumentException se a tarefa não for encontrada
     */
    public Task execute(Long taskId) {
        // Optional.orElseThrow: forma idiomática de "busca ou lança erro".
        // A mensagem de erro é regra de aplicação — fica aqui, não no controller.
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tarefa não encontrada com ID: " + taskId));

        // Delega o comportamento para a entidade.
        // Se já estiver completa, Task lança IllegalStateException.
        task.complete();

        // Persiste o estado atualizado.
        return taskRepository.update(task);
    }
}
