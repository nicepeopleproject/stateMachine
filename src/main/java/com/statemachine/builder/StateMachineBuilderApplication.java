package com.statemachine.builder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class StateMachineBuilderApplication {

    public static void main(String[] args) {
        SpringApplication.run(StateMachineBuilderApplication.class, args);
    }
}
