package com.statemachine.builder.repository;

import com.statemachine.builder.model.TemplateTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TemplateTransitionRepository extends JpaRepository<TemplateTransition, UUID> {
    List<TemplateTransition> findByTemplateId(UUID templateId);
}
