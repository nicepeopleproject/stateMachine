package com.statemachine.builder.repository;

import com.statemachine.builder.model.StateMachineHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StateMachineHistoryRepository extends JpaRepository<StateMachineHistory, UUID> {
    List<StateMachineHistory> findByInstanceIdOrderByTransitionTimeDesc(UUID instanceId);
    Page<StateMachineHistory> findByInstanceId(UUID instanceId, Pageable pageable);
}
