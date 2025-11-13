package com.statemachine.builder.service;

import com.statemachine.builder.model.StateMachineHistory;
import com.statemachine.builder.model.StateMachineInstance;
import com.statemachine.builder.model.StateMachineTemplate;
import com.statemachine.builder.repository.StateMachineHistoryRepository;
import com.statemachine.builder.repository.StateMachineInstanceRepository;
import com.statemachine.builder.repository.StateMachineTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StateMachineInstanceService {

    private final StateMachineInstanceRepository instanceRepository;
    private final StateMachineTemplateRepository templateRepository;
    private final StateMachineHistoryRepository historyRepository;

    @Transactional
    public StateMachineInstance createInstance(UUID templateId, String projectId) {
        log.info("Creating instance for template {} and project {}", templateId, projectId);

        StateMachineTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));

        StateMachineInstance instance = new StateMachineInstance();
        instance.setTemplate(template);
        instance.setProjectId(projectId);
        instance.setCurrentState(template.getInitialState());
        instance.setStatus(StateMachineInstance.InstanceStatus.ACTIVE);
        instance.setContextData(new HashMap<>());

        StateMachineInstance saved = instanceRepository.save(instance);

        // Record initial state
        recordHistory(saved, null, template.getInitialState(), null, null);

        log.info("Instance created with id: {}", saved.getId());
        return saved;
    }

    @Transactional
    public void transitionState(UUID instanceId, String eventName, Map<String, Object> eventData) {
        log.info("Transitioning instance {} with event {}", instanceId, eventName);

        StateMachineInstance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new IllegalArgumentException("Instance not found: " + instanceId));

        if (instance.getStatus() != StateMachineInstance.InstanceStatus.ACTIVE) {
            throw new IllegalStateException("Instance is not active");
        }

        String currentState = instance.getCurrentState();

        // This is a simplified version - actual transition logic will be handled by Spring State Machine
        // For now, we just record the event
        recordHistory(instance, currentState, currentState, eventName, eventData);
    }

    @Transactional
    public void updateInstanceState(UUID instanceId, String newState, String eventName, Map<String, Object> eventData) {
        log.info("Updating instance {} state to {}", instanceId, newState);

        StateMachineInstance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new IllegalArgumentException("Instance not found: " + instanceId));

        String oldState = instance.getCurrentState();
        instance.setCurrentState(newState);
        instanceRepository.save(instance);

        recordHistory(instance, oldState, newState, eventName, eventData);
    }

    @Transactional(readOnly = true)
    public StateMachineInstance getInstance(UUID instanceId) {
        return instanceRepository.findById(instanceId)
                .orElseThrow(() -> new IllegalArgumentException("Instance not found: " + instanceId));
    }

    @Transactional(readOnly = true)
    public List<StateMachineInstance> getInstancesByProject(String projectId) {
        return instanceRepository.findByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public List<StateMachineHistory> getInstanceHistory(UUID instanceId) {
        return historyRepository.findByInstanceIdOrderByTransitionTimeDesc(instanceId);
    }

    private void recordHistory(StateMachineInstance instance, String fromState, String toState,
                               String eventName, Map<String, Object> eventData) {
        StateMachineHistory history = new StateMachineHistory();
        history.setInstance(instance);
        history.setFromState(fromState);
        history.setToState(toState);
        history.setEventName(eventName);
        history.setEventData(eventData);
        historyRepository.save(history);
    }
}
