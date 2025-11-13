package com.statemachine.builder.controller;

import com.statemachine.builder.dto.CreateTemplateRequest;
import com.statemachine.builder.dto.StateMachineTemplateDto;
import com.statemachine.builder.service.StateMachineTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
@Tag(name = "State Machine Templates", description = "API for managing state machine templates")
public class TemplateController {

    private final StateMachineTemplateService templateService;

    @PostMapping
    @Operation(summary = "Create a new state machine template")
    public ResponseEntity<StateMachineTemplateDto> createTemplate(@Valid @RequestBody CreateTemplateRequest request) {
        StateMachineTemplateDto template = templateService.createTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(template);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a template by ID")
    public ResponseEntity<StateMachineTemplateDto> getTemplate(@PathVariable UUID id) {
        StateMachineTemplateDto template = templateService.getTemplate(id);
        return ResponseEntity.ok(template);
    }

    @GetMapping
    @Operation(summary = "Get all templates")
    public ResponseEntity<List<StateMachineTemplateDto>> getAllTemplates() {
        List<StateMachineTemplateDto> templates = templateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a template")
    public ResponseEntity<Void> deleteTemplate(@PathVariable UUID id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
