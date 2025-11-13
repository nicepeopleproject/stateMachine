package com.statemachine.builder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateRequest {
    @NotBlank(message = "Template name is required")
    private String name;

    private String description;

    @NotBlank(message = "Initial state is required")
    private String initialState;

    @NotNull(message = "States list is required")
    private List<TemplateStateDto> states = new ArrayList<>();

    @NotNull(message = "Transitions list is required")
    private List<TemplateTransitionDto> transitions = new ArrayList<>();
}
