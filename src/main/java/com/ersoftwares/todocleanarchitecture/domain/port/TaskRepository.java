package com.ersoftwares.todocleanarchitecture.domain.port;

import com.ersoftwares.todocleanarchitecture.domain.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Porta de saída (Output Port) — padrão Ports & Adapters.
 * <p>
 * Esta interface é definida na camada domain e representa o CONTRATO
 * que qualquer mecanismo de persistência deve cumprir.
 * <p>
 * O domain define O QUE precisa (salvar, buscar, atualizar).
 * A infrastructure define COMO faz (SQL, MongoDB, arquivo, memória).
 * <p>
 * Isso é a Inversão de Dependência em ação: a camada interna (domain)
 * define a interface; a camada externa (infrastructure) a implementa.
 * <p>
 * Optional<Task> em findById é melhor que retornar null:
 * força o chamador a lidar explicitamente com "não encontrado".
 */
public interface TaskRepository {

    Task save(Task task);

    List<Task> findAll();

    Optional<Task> findById(Long id);

    Task update(Task task);

    void delete(Long id);
}
