package com.statemachine.builder.repository;

import com.statemachine.builder.model.TemplateState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TemplateStateRepository extends JpaRepository<TemplateState, UUID> {
    List<TemplateState> findByTemplateId(UUID templateId);
}
