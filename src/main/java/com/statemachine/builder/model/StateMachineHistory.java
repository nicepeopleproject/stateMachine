package com.statemachine.builder.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "state_machine_history", indexes = {
    @Index(name = "idx_history_instance", columnList = "instance_id"),
    @Index(name = "idx_history_time", columnList = "transition_time")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateMachineHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id", nullable = false)
    private StateMachineInstance instance;

    @Column(name = "from_state")
    private String fromState;

    @Column(name = "to_state", nullable = false)
    private String toState;

    @Column(name = "event_name")
    private String eventName;

    @Type(JsonBinaryType.class)
    @Column(name = "event_data", columnDefinition = "jsonb")
    private Map<String, Object> eventData;

    @CreationTimestamp
    @Column(name = "transition_time", nullable = false, updatable = false)
    private LocalDateTime transitionTime;
}
