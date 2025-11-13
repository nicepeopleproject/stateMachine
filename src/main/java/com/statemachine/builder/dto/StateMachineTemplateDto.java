package com.statemachine.builder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateMachineTemplateDto {
    private UUID id;
    private String name;
    private String description;
    private String initialState;
    private List<TemplateStateDto> states = new ArrayList<>();
    private List<TemplateTransitionDto> transitions = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
