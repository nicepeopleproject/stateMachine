package com.statemachine.builder.dto;

import com.statemachine.builder.model.TemplateState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateStateDto {
    private UUID id;
    private String stateName;
    private TemplateState.StateType stateType;
    private Map<String, Object> entryActions;
    private Map<String, Object> exitActions;
    private Integer positionX;
    private Integer positionY;
}
