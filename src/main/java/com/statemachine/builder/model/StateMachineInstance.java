package com.statemachine.builder.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "state_machine_instances", indexes = {
    @Index(name = "idx_instance_project", columnList = "project_id"),
    @Index(name = "idx_instance_template", columnList = "template_id"),
    @Index(name = "idx_instance_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateMachineInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private StateMachineTemplate template;

    @Column(name = "project_id", nullable = false)
    private String projectId;

    @Column(name = "current_state", nullable = false)
    private String currentState;

    @Type(JsonBinaryType.class)
    @Column(name = "context_data", columnDefinition = "jsonb")
    private Map<String, Object> contextData;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InstanceStatus status = InstanceStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum InstanceStatus {
        ACTIVE,
        PAUSED,
        COMPLETED,
        FAILED
    }
}
