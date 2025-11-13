package com.statemachine.builder.service;

import com.statemachine.builder.dto.*;
import com.statemachine.builder.model.StateMachineTemplate;
import com.statemachine.builder.model.TemplateState;
import com.statemachine.builder.model.TemplateTransition;
import com.statemachine.builder.repository.StateMachineTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StateMachineTemplateService {

    private final StateMachineTemplateRepository templateRepository;

    @Transactional
    public StateMachineTemplateDto createTemplate(CreateTemplateRequest request) {
        log.info("Creating new state machine template: {}", request.getName());

        if (templateRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Template with name '" + request.getName() + "' already exists");
        }

        StateMachineTemplate template = new StateMachineTemplate();
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setInitialState(request.getInitialState());

        // Add states
        for (TemplateStateDto stateDto : request.getStates()) {
            TemplateState state = new TemplateState();
            state.setTemplate(template);
            state.setStateName(stateDto.getStateName());
            state.setStateType(stateDto.getStateType() != null ? stateDto.getStateType() : TemplateState.StateType.NORMAL);
            state.setEntryActions(stateDto.getEntryActions());
            state.setExitActions(stateDto.getExitActions());
            state.setPositionX(stateDto.getPositionX() != null ? stateDto.getPositionX() : 0);
            state.setPositionY(stateDto.getPositionY() != null ? stateDto.getPositionY() : 0);
            template.getStates().add(state);
        }

        // Add transitions
        for (TemplateTransitionDto transitionDto : request.getTransitions()) {
            TemplateTransition transition = new TemplateTransition();
            transition.setTemplate(template);
            transition.setSourceState(transitionDto.getSourceState());
            transition.setTargetState(transitionDto.getTargetState());
            transition.setEventName(transitionDto.getEventName());
            transition.setGuardExpression(transitionDto.getGuardExpression());
            transition.setActions(transitionDto.getActions());
            transition.setOrderIndex(transitionDto.getOrderIndex() != null ? transitionDto.getOrderIndex() : 0);
            template.getTransitions().add(transition);
        }

        StateMachineTemplate saved = templateRepository.save(template);
        log.info("Template created successfully with id: {}", saved.getId());

        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public StateMachineTemplateDto getTemplate(UUID id) {
        StateMachineTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found with id: " + id));
        return convertToDto(template);
    }

    @Transactional(readOnly = true)
    public List<StateMachineTemplateDto> getAllTemplates() {
        return templateRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTemplate(UUID id) {
        log.info("Deleting template with id: {}", id);
        templateRepository.deleteById(id);
    }

    private StateMachineTemplateDto convertToDto(StateMachineTemplate template) {
        StateMachineTemplateDto dto = new StateMachineTemplateDto();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setDescription(template.getDescription());
        dto.setInitialState(template.getInitialState());
        dto.setCreatedAt(template.getCreatedAt());
        dto.setUpdatedAt(template.getUpdatedAt());

        dto.setStates(template.getStates().stream()
                .map(this::convertStateToDto)
                .collect(Collectors.toList()));

        dto.setTransitions(template.getTransitions().stream()
                .map(this::convertTransitionToDto)
                .collect(Collectors.toList()));

        return dto;
    }

    private TemplateStateDto convertStateToDto(TemplateState state) {
        TemplateStateDto dto = new TemplateStateDto();
        dto.setId(state.getId());
        dto.setStateName(state.getStateName());
        dto.setStateType(state.getStateType());
        dto.setEntryActions(state.getEntryActions());
        dto.setExitActions(state.getExitActions());
        dto.setPositionX(state.getPositionX());
        dto.setPositionY(state.getPositionY());
        return dto;
    }

    private TemplateTransitionDto convertTransitionToDto(TemplateTransition transition) {
        TemplateTransitionDto dto = new TemplateTransitionDto();
        dto.setId(transition.getId());
        dto.setSourceState(transition.getSourceState());
        dto.setTargetState(transition.getTargetState());
        dto.setEventName(transition.getEventName());
        dto.setGuardExpression(transition.getGuardExpression());
        dto.setActions(transition.getActions());
        dto.setOrderIndex(transition.getOrderIndex());
        return dto;
    }
}
