package com.ersoftwares.todocleanarchitecture.domain.model;

import java.time.LocalDateTime;

/**
 * Entidade de domínio pura.
 * <p>
 * NENHUMA anotação de framework aqui — nenhum @Entity, nenhum @Column.
 * Esta classe não sabe que existe Spring, JPA, banco de dados ou HTTP.
 * Ela representa o conceito de "Tarefa" segundo as regras do negócio.
 * <p>
 * Note que usamos Long como ID porque é o tipo natural do domínio.
 * A geração desse ID é responsabilidade da infraestrutura.
 */
public class Task {

    private Long id;
    private String title;
    private boolean completed;
    private LocalDateTime createdAt;

    /**
     * Construtor usado para CRIAR uma nova tarefa (sem ID ainda).
     * O ID será atribuído pela infraestrutura (banco de dados) ao persistir.
     */
    public Task(String title) {
        if (title == null || title.isBlank()) {
            // Regra de negócio: título obrigatório.
            // O erro é lançado na própria entidade, não no controller.
            throw new IllegalArgumentException("O título da tarefa não pode ser vazio.");
        }
        this.title = title.trim();
        this.completed = false;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Construtor usado para RECONSTITUIR uma tarefa que já existe no banco.
     * Quando carregamos do banco, precisamos restaurar o estado completo,
     * incluindo o ID e o estado 'completed' que pode já ser true.
     */
    public Task(Long id, String title, boolean completed, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    /**
     * Comportamento de domínio: marcar como completa.
     * A lógica "não pode completar o que já está completo" fica aqui,
     * não no use case nem no controller.
     */
    public void complete() {
        if (this.completed) {
            throw new IllegalStateException("Esta tarefa já está concluída.");
        }
        this.completed = true;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────
    // Não usamos Lombok na domain/ intencionalmente — queremos zero dependências.

    public Long getId() {
        return id;
    }

    // Setter de ID: só a infraestrutura deve chamar após persistir.
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
