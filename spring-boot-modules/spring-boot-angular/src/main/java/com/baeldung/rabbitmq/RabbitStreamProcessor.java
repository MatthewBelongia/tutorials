package com.baeldung.rabbitmq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface RabbitStreamProcessor {
	
	String RABBIT_INPUT_QUEUE = "test-queue";
	String RABBIT_OUTPUT_QUEUE = "test-queue-output";
	
	@Input(RABBIT_INPUT_QUEUE)
	MessageChannel rabbitInputQueue();
	
	@Output(RABBIT_OUTPUT_QUEUE)
	MessageChannel rabbitOutputQueue();

}
