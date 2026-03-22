package com.ersoftwares.todocleanarchitecture.infrastructure;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade JPA — modelo de persistência.
 * <p>
 * Por que separar TaskEntity de Task (domínio)?
 * <p>
 * Se usarmos a Task do domínio diretamente como @Entity:
 * - O domínio passaria a depender do JPA (viola a regra de dependência)
 * - @Entity força um construtor sem argumentos (quebraria nosso design)
 * - Campos como @Version (controle de concorrência) poluiriam o domínio
 * - Mudanças no banco (renomear coluna) afetariam o domínio
 * <p>
 * Mantendo separado:
 * - Domain fica puro, sem anotações de framework
 * - Podemos ter campos técnicos (version, deletedAt) só na persistência
 * - A conversão TaskEntity ↔ Task acontece no Adapter (próxima classe)
 *
 * @Data: gera getters, setters, equals, hashCode, toString
 * @NoArgsConstructor: construtor sem args — obrigatório pelo JPA
 */
@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class TaskEntity {

    /**
     * @Id marca a chave primária.
     * @GeneratedValue com IDENTITY delega a geração ao banco (auto_increment).
     * Assim o domínio nunca precisa gerar IDs — o banco cuida disso.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column com nullable=false e length cria um NOT NULL VARCHAR(200).
     * Isso adiciona restrição no DDL gerado pelo Hibernate.
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * Primitivos boolean nunca são null — seguro usar sem nullable.
     */
    @Column(nullable = false)
    private boolean completed;

    /**
     * LocalDateTime mapeia para TIMESTAMP no banco.
     * updatable=false: uma vez gravado, não atualiza mais (imutável).
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Construtor de conveniência — facilita a criação no Adapter.
    public TaskEntity(String title, boolean completed, LocalDateTime createdAt) {
        this.title = title;
        this.completed = completed;
        this.createdAt = createdAt;
    }
}
