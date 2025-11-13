package com.company.statemachine.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@JmixEntity
@Table(name = "TRANSITION")
@Entity
public class Transition {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @InstanceName
    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @JoinColumn(name = "TEMPLATE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private StateMachineTemplate template;

    @JoinColumn(name = "FROM_STATE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private State fromState;

    @JoinColumn(name = "TO_STATE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private State toState;

    @Column(name = "EVENT_NAME", nullable = false)
    @NotNull
    private String eventName;

    @Column(name = "CONDITION_SCRIPT")
    @Lob
    private String conditionScript;

    @Column(name = "ACTION_SCRIPT")
    @Lob
    private String actionScript;

    public String getActionScript() {
        return actionScript;
    }

    public void setActionScript(String actionScript) {
        this.actionScript = actionScript;
    }

    public String getConditionScript() {
        return conditionScript;
    }

    public void setConditionScript(String conditionScript) {
        this.conditionScript = conditionScript;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public State getToState() {
        return toState;
    }

    public void setToState(State toState) {
        this.toState = toState;
    }

    public State getFromState() {
        return fromState;
    }

    public void setFromState(State fromState) {
        this.fromState = fromState;
    }

    public StateMachineTemplate getTemplate() {
        return template;
    }

    public void setTemplate(StateMachineTemplate template) {
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
