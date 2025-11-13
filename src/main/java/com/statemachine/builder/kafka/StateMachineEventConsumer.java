package com.statemachine.builder.kafka;

import com.statemachine.builder.dto.StateMachineEventDto;
import com.statemachine.builder.statemachine.StateMachineFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StateMachineEventConsumer {

    private final StateMachineFactory stateMachineFactory;

    @KafkaListener(topics = "${kafka.topic.state-machine-events:state-machine-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeEvent(StateMachineEventDto event) {
        log.info("Received event: {} for instance: {}", event.getEventName(), event.getInstanceId());

        try {
            if (event.getInstanceId() != null) {
                stateMachineFactory.sendEvent(event.getInstanceId(), event.getEventName(), event.getEventData());
                log.info("Event processed successfully");
            } else {
                log.warn("Event has no instance ID, skipping");
            }
        } catch (Exception e) {
            log.error("Error processing event", e);
            // In production, you might want to send to a dead letter queue
        }
    }
}
