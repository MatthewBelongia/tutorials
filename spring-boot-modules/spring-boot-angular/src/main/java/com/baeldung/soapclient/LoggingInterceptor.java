package com.baeldung.soapclient;

import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

public class LoggingInterceptor implements ClientInterceptor{

	@Override
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {
		// TODO Auto-generated method stub
		
	}

}
