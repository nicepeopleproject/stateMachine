package com.statemachine.builder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "template_transitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateTransition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    @JsonIgnore
    private StateMachineTemplate template;

    @Column(name = "source_state", nullable = false)
    private String sourceState;

    @Column(name = "target_state", nullable = false)
    private String targetState;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "guard_expression", columnDefinition = "TEXT")
    private String guardExpression;

    @Type(JsonBinaryType.class)
    @Column(name = "actions", columnDefinition = "jsonb")
    private Map<String, Object> actions;

    @Column(name = "order_index")
    private Integer orderIndex = 0;
}
