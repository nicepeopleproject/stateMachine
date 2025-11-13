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
@Table(name = "template_states", uniqueConstraints = {
    @UniqueConstraint(name = "uk_template_state_name", columnNames = {"template_id", "state_name"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateState {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    @JsonIgnore
    private StateMachineTemplate template;

    @Column(name = "state_name", nullable = false)
    private String stateName;

    @Enumerated(EnumType.STRING)
    @Column(name = "state_type", nullable = false)
    private StateType stateType = StateType.NORMAL;

    @Type(JsonBinaryType.class)
    @Column(name = "entry_actions", columnDefinition = "jsonb")
    private Map<String, Object> entryActions;

    @Type(JsonBinaryType.class)
    @Column(name = "exit_actions", columnDefinition = "jsonb")
    private Map<String, Object> exitActions;

    @Column(name = "position_x")
    private Integer positionX = 0;

    @Column(name = "position_y")
    private Integer positionY = 0;

    public enum StateType {
        NORMAL,
        INITIAL,
        FINAL,
        CHOICE,
        FORK,
        JOIN
    }
}
