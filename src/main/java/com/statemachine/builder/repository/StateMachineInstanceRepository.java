package com.statemachine.builder.repository;

import com.statemachine.builder.model.StateMachineInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StateMachineInstanceRepository extends JpaRepository<StateMachineInstance, UUID> {
    List<StateMachineInstance> findByProjectId(String projectId);
    List<StateMachineInstance> findByTemplateId(UUID templateId);
    Optional<StateMachineInstance> findByProjectIdAndTemplateId(String projectId, UUID templateId);
    List<StateMachineInstance> findByStatus(StateMachineInstance.InstanceStatus status);
}
