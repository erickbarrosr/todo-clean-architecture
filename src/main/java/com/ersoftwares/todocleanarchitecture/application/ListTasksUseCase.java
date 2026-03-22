package com.ersoftwares.todocleanarchitecture.application;

import com.ersoftwares.todocleanarchitecture.domain.model.Task;
import com.ersoftwares.todocleanarchitecture.domain.port.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use Case: listar todas as tarefas.
 * <p>
 * Simples agora, mas aqui é onde adicionaríamos lógica futura:
 * filtrar por status, paginar, ordenar por data — sem tocar no domain
 * nem nos controllers.
 */
@Service
public class ListTasksUseCase {

    private final TaskRepository taskRepository;

    public ListTasksUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> execute() {
        return taskRepository.findAll();
    }
}
