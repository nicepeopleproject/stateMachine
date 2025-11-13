package com.statemachine.builder.controller;

import com.statemachine.builder.dto.StateMachineEventDto;
import com.statemachine.builder.model.StateMachineHistory;
import com.statemachine.builder.model.StateMachineInstance;
import com.statemachine.builder.service.StateMachineInstanceService;
import com.statemachine.builder.statemachine.StateMachineFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/instances")
@RequiredArgsConstructor
@Tag(name = "State Machine Instances", description = "API for managing state machine instances")
public class InstanceController {

    private final StateMachineInstanceService instanceService;
    private final StateMachineFactory stateMachineFactory;

    @PostMapping
    @Operation(summary = "Create a new state machine instance")
    public ResponseEntity<StateMachineInstance> createInstance(
            @RequestParam UUID templateId,
            @RequestParam String projectId) {
        try {
            StateMachineInstance instance = instanceService.createInstance(templateId, projectId);
            stateMachineFactory.createStateMachine(instance);
            return ResponseEntity.status(HttpStatus.CREATED).body(instance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create state machine instance", e);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get instance by ID")
    public ResponseEntity<StateMachineInstance> getInstance(@PathVariable UUID id) {
        StateMachineInstance instance = instanceService.getInstance(id);
        return ResponseEntity.ok(instance);
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get instances by project ID")
    public ResponseEntity<List<StateMachineInstance>> getInstancesByProject(@PathVariable String projectId) {
        List<StateMachineInstance> instances = instanceService.getInstancesByProject(projectId);
        return ResponseEntity.ok(instances);
    }

    @PostMapping("/{id}/event")
    @Operation(summary = "Send event to state machine instance")
    public ResponseEntity<Void> sendEvent(
            @PathVariable UUID id,
            @RequestBody StateMachineEventDto eventDto) {
        stateMachineFactory.sendEvent(id, eventDto.getEventName(), eventDto.getEventData());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "Get instance transition history")
    public ResponseEntity<List<StateMachineHistory>> getHistory(@PathVariable UUID id) {
        List<StateMachineHistory> history = instanceService.getInstanceHistory(id);
        return ResponseEntity.ok(history);
    }
}
