package com.baeldung.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AroundAspect {

	AspectLoggingService aspectLoggingService;
	ObjectMapper mapper;

	public AroundAspect(AspectLoggingService aspectLoggingService, ObjectMapper mapper) {
		this.aspectLoggingService = aspectLoggingService;
		this.mapper = mapper;
	}

	@Around("@annotation(com.baeldung.aspect.AspectLogger)")
	public void logAround(ProceedingJoinPoint joinPoint) throws Throwable{
		
		final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String logName = getLogName(signature);
		
		String methodDetails = "Method Name:: " + signature.getName() + " () Class Name :: " + joinPoint.getTarget().getClass().getName();
		Object[] arguments = joinPoint.getArgs();
		log.info(methodDetails);
		aspectLoggingService.publishToLoggingQueue(AspectLogPojo.builder().status("ENTERED").id("123").build(),arguments[0]);
		
		try {
			joinPoint.proceed();
			aspectLoggingService.publishToLoggingQueue(AspectLogPojo.builder().status("SUCCESS").id("123").build(),arguments[0]);
			
		}catch(Throwable e) {
			aspectLoggingService.publishToLoggingQueue(AspectLogPojo.builder().status("ERROR").id("123").build(),arguments[0]);
			
		}
		
	}

	private String getLogName(MethodSignature signature) {
		Method method = signature.getMethod();
		AspectLogger logAnnotation = method.getAnnotation(AspectLogger.class);
		return logAnnotation.value();
	}

}
