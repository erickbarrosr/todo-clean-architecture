package com.ersoftwares.todocleanarchitecture.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface Spring Data JPA.
 * <p>
 * Spring Data gera a implementação em tempo de execução — nós não
 * escrevemos nenhum SQL. Basta declarar a interface.
 * <p>
 * JpaRepository<TaskEntity, Long> fornece automaticamente:
 * - save(), findById(), findAll(), delete(), count(), existsById()...
 * <p>
 * Esta interface SÓ trabalha com TaskEntity (camada de persistência).
 * Ela NUNCA é usada diretamente pelo domínio ou pelos use cases —
 * isso é responsabilidade do TaskRepositoryAdapter a seguir.
 */
public interface SpringDataTaskRepository extends JpaRepository<TaskEntity, Long> {
    // Nenhum método adicional necessário por enquanto.
    // Poderíamos adicionar: List<TaskEntity> findByCompleted(boolean completed);
}
