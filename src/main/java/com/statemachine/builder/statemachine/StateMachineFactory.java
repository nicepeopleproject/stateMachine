package com.statemachine.builder.statemachine;

import com.statemachine.builder.model.StateMachineInstance;
import com.statemachine.builder.model.StateMachineTemplate;
import com.statemachine.builder.model.TemplateTransition;
import com.statemachine.builder.service.StateMachineInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class StateMachineFactory {

    private final StateMachineInstanceService instanceService;
    private final Map<UUID, StateMachine<String, String>> runningMachines = new ConcurrentHashMap<>();

    public StateMachine<String, String> createStateMachine(StateMachineInstance instance) throws Exception {
        log.info("Creating state machine for instance {}", instance.getId());

        StateMachineTemplate template = instance.getTemplate();
        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();

        // Configure state machine
        builder.configureConfiguration()
                .withConfiguration()
                .autoStartup(false)
                .listener(new StateMachineListenerAdapter<String, String>() {
                    @Override
                    public void stateChanged(State<String, String> from, State<String, String> to) {
                        log.info("State changed from {} to {}",
                                from != null ? from.getId() : "null",
                                to != null ? to.getId() : "null");

                        if (to != null) {
                            instanceService.updateInstanceState(
                                    instance.getId(),
                                    to.getId(),
                                    null,
                                    instance.getContextData()
                            );
                        }
                    }
                });

        // Configure states
        Set<String> stateIds = new HashSet<>();
        template.getStates().forEach(state -> stateIds.add(state.getStateName()));

        builder.configureStates()
                .withStates()
                .initial(template.getInitialState())
                .states(stateIds);

        // Configure transitions
        var transitionsConfig = builder.configureTransitions();
        for (TemplateTransition transition : template.getTransitions()) {
            transitionsConfig
                    .withExternal()
                    .source(transition.getSourceState())
                    .target(transition.getTargetState())
                    .event(transition.getEventName());
        }

        StateMachine<String, String> stateMachine = builder.build();
        stateMachine.start();

        runningMachines.put(instance.getId(), stateMachine);

        return stateMachine;
    }

    public StateMachine<String, String> getStateMachine(UUID instanceId) {
        return runningMachines.get(instanceId);
    }

    public void sendEvent(UUID instanceId, String event, Map<String, Object> eventData) {
        StateMachine<String, String> machine = runningMachines.get(instanceId);
        if (machine != null) {
            log.info("Sending event {} to instance {}", event, instanceId);
            machine.sendEvent(event);

            // Update event data in instance
            instanceService.transitionState(instanceId, event, eventData);
        } else {
            log.warn("State machine not found for instance {}", instanceId);
            throw new IllegalStateException("State machine not found for instance: " + instanceId);
        }
    }

    public void stopStateMachine(UUID instanceId) {
        StateMachine<String, String> machine = runningMachines.remove(instanceId);
        if (machine != null) {
            machine.stop();
            log.info("Stopped state machine for instance {}", instanceId);
        }
    }
}
