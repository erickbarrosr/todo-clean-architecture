package com.ersoftwares.todocleanarchitecture.application.usecase;

import com.ersoftwares.todocleanarchitecture.domain.model.Task;
import com.ersoftwares.todocleanarchitecture.domain.port.TaskRepository;
import org.springframework.stereotype.Service;

/**
 * Use Case: adicionar uma nova tarefa.
 *
 * @Service é a única anotação Spring aqui — necessária para que o Spring
 * gerencie o ciclo de vida desta classe e injete o repositório.
 * Mas note: ela não usa nenhuma funcionalidade específica de web ou banco.
 * <p>
 * O repositório é injetado via construtor (melhor prática em Spring):
 * - Torna as dependências explícitas e obrigatórias
 * - Facilita testes (basta passar um mock no construtor)
 * - Imutabilidade: o campo pode ser 'final'
 */
@Service
public class AddTaskUseCase {

    // 'final' garante que o repositório não pode ser substituído após construção.
    private final TaskRepository taskRepository;

    // Spring detecta automaticamente construtores com um único parâmetro
    // e faz a injeção sem precisar de @Autowired (desde Spring 4.3).
    public AddTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Executa o caso de uso.
     *
     * @param title o título da nova tarefa
     * @return a tarefa criada e persistida (já com ID)
     */
    public Task execute(String title) {
        // Cria a entidade — a própria Task valida o título.
        Task task = new Task(title);

        // Persiste via porta de saída.
        // Não sabemos (e não precisamos saber) se é H2, PostgreSQL ou MongoDB.
        return taskRepository.save(task);
    }
}
