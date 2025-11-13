package com.statemachine.builder.repository;

import com.statemachine.builder.model.StateMachineTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StateMachineTemplateRepository extends JpaRepository<StateMachineTemplate, UUID> {
    Optional<StateMachineTemplate> findByName(String name);
    boolean existsByName(String name);
}
