package com.statemachine.builder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateTransitionDto {
    private UUID id;
    private String sourceState;
    private String targetState;
    private String eventName;
    private String guardExpression;
    private Map<String, Object> actions;
    private Integer orderIndex;
}
