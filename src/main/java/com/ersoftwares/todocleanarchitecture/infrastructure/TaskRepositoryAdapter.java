package com.ersoftwares.todocleanarchitecture.infrastructure;

import com.ersoftwares.todocleanarchitecture.domain.model.Task;
import com.ersoftwares.todocleanarchitecture.domain.port.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de saída (Output Adapter) — padrão Ports & Adapters.
 * <p>
 * Esta classe é a ponte entre o mundo do domínio (Task) e o mundo
 * da persistência (TaskEntity + JPA).
 * <p>
 * Ela implementa TaskRepository (porta definida no domain/) usando
 * SpringDataTaskRepository (recurso da infrastructure/).
 * <p>
 * Responsabilidades:
 * 1. Converter Task → TaskEntity antes de persistir
 * 2. Converter TaskEntity → Task após buscar do banco
 * 3. Delegar as operações de banco para o SpringDataTaskRepository
 *
 * @Component indica para o Spring que esta classe deve ser instanciada
 * e gerenciada como bean. Quando o Spring precisar de uma implementação
 * de TaskRepository, vai encontrar este @Component automaticamente.
 */
@Component
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepository {

    private final SpringDataTaskRepository jpaRepository;

    @Override
    public Task save(Task task) {
        // Converte domínio → entidade JPA para persistir.
        TaskEntity entity = toEntity(task);
        TaskEntity savedEntity = jpaRepository.save(entity);
        // Converte de volta entidade JPA → domínio para retornar.
        return toDomain(savedEntity);
    }

    @Override
    public List<Task> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain) // Converte cada TaskEntity para Task
                .toList();
    }

    @Override
    public Optional<Task> findById(Long id) {
        // map() dentro do Optional transforma o conteúdo se presente.
        // Se o Optional estiver vazio, map() não faz nada — retorna Optional.empty().
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Task update(Task task) {
        // JPA: save() com ID existente faz UPDATE; com ID null faz INSERT.
        TaskEntity entity = toEntity(task);
        TaskEntity updatedEntity = jpaRepository.save(entity);
        return toDomain(updatedEntity);
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }

    // ─── Métodos de conversão (mapping) ────────────────────────────────────────

    /**
     * Converte entidade de domínio para entidade JPA.
     * Note que o ID pode ser null (novo) ou existente (update).
     */
    private TaskEntity toEntity(Task task) {
        TaskEntity entity = new TaskEntity(
                task.getTitle(),
                task.isCompleted(),
                task.getCreatedAt()
        );
        entity.setId(task.getId()); // null para novas tarefas, número para updates
        return entity;
    }

    /**
     * Converte entidade JPA para entidade de domínio.
     * Usa o construtor de reconstituição do Task (com ID e estado completo).
     */
    private Task toDomain(TaskEntity entity) {
        return new Task(
                entity.getId(),
                entity.getTitle(),
                entity.isCompleted(),
                entity.getCreatedAt()
        );
    }
}
