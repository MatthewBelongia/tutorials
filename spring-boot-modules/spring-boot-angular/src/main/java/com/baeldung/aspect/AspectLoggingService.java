package com.baeldung.aspect;

@FunctionalInterface
public interface AspectLoggingService {
	
	void publishToLoggingQueue(AspectLogPojo logPojo, Object param);

}
