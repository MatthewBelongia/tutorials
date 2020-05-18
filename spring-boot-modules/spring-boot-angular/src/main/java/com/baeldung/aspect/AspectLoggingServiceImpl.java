package com.baeldung.aspect;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import com.baeldung.rabbitmq.RabbitStreamProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AspectLoggingServiceImpl implements AspectLoggingService {

	private RabbitStreamProcessor processor;
	private ObjectMapper mapper;

	public AspectLoggingServiceImpl(RabbitStreamProcessor processor, ObjectMapper mapper) {
		this.processor = processor;
		this.mapper = mapper;
	}

	@Override
	public void publishToLoggingQueue(AspectLogPojo logPojo, Object param) {

		AspectLogPojo logEvent = null;
		log.info("input param {} ", param);
		logEvent = logPojo;

		processor.rabbitLogQueue().send(MessageBuilder.withPayload(logEvent)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE).build());

	}

}
