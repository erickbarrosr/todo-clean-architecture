package com.ersoftwares.todocleanarchitecture.adapter.controller;

import com.ersoftwares.todocleanarchitecture.adapter.dto.CreateTaskRequest;
import com.ersoftwares.todocleanarchitecture.adapter.dto.TaskResponse;
import com.ersoftwares.todocleanarchitecture.application.usecase.AddTaskUseCase;
import com.ersoftwares.todocleanarchitecture.application.usecase.CompleteTaskUseCase;
import com.ersoftwares.todocleanarchitecture.application.usecase.ListTasksUseCase;
import com.ersoftwares.todocleanarchitecture.domain.model.Task;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Adaptador de entrada: traduz requisições HTTP em chamadas de use cases.
 * <p>
 * O controller tem DUAS únicas responsabilidades:
 * 1. Interpretar a requisição HTTP (corpo, path variables, parâmetros)
 * 2. Delegar para o use case correto e formatar a resposta HTTP
 * <p>
 * NÃO há lógica de negócio aqui. Se você se pegar escrevendo um 'if'
 * de regra de negócio no controller, ele pertence a um use case.
 *
 * @RestController = @Controller + @ResponseBody (retorna JSON automaticamente)
 * @RequestMapping define o prefixo de URL para todos os endpoints da classe.
 * @RequiredArgsConstructor do Lombok gera o construtor com todos os campos 'final'.
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    // Injetamos use cases, não repositórios — o controller não acessa o banco.
    private final AddTaskUseCase addTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;

    /**
     * POST /api/tasks
     * Cria uma nova tarefa.
     *
     * @Valid dispara a validação do Bean Validation no CreateTaskRequest.
     * Se falhar, Spring retorna 400 automaticamente antes de entrar no método.
     * ResponseEntity<T> nos dá controle explícito do status HTTP.
     */
    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest request) {
        Task task = addTaskUseCase.execute(request.getTitle());
        // 201 Created é o status correto para criação de recursos.
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TaskResponse.from(task));
    }

    /**
     * GET /api/tasks
     * Lista todas as tarefas.
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> listAll() {
        List<TaskResponse> responses = listTasksUseCase.execute()
                .stream()
                // Converte cada Task em TaskResponse usando o factory method.
                .map(TaskResponse::from)
                .toList(); // Java 16+: toList() é imutável e mais conciso que collect()

        return ResponseEntity.ok(responses);
    }

    /**
     * PATCH /api/tasks/{id}/complete
     * Marca uma tarefa como concluída.
     * <p>
     * PATCH é o verbo correto para atualizações parciais (só o status muda).
     * PUT seria para substituir o recurso inteiro.
     *
     * @PathVariable extrai o {id} da URL.
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> complete(@PathVariable Long id) {
        Task task = completeTaskUseCase.execute(id);
        return ResponseEntity.ok(TaskResponse.from(task));
    }
}
