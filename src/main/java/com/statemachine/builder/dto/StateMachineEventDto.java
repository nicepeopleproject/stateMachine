package com.statemachine.builder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateMachineEventDto {
    private UUID instanceId;
    private String projectId;
    private String eventName;
    private Map<String, Object> eventData;
}
