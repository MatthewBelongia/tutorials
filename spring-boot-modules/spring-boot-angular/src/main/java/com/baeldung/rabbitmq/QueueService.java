package com.baeldung.rabbitmq;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QueueService {
	
	@StreamListener(RabbitStreamProcessor.RABBIT_INPUT_QUEUE)
	public void consumeTestQueue(Message<String> message) {
		
		log.info("consumed from Queue");
	}

}
