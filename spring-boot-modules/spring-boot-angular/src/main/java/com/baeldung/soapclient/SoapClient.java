package com.baeldung.soapclient;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.w3._2005._08.addressing.AttributedURIType;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3._2005._08.addressing.ObjectFactory;
import org.w3._2005._08.addressing.RelatesToType;

import com.baeldung.soapclient.dto.SimpleSoapRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SoapClient extends WebServiceGatewaySupport {

	String soapServerEndpointUrl = "http://localhost:8080/ws";

	public void marshalAndSendSOAP(SimpleSoapRequest request) {

		getWebServiceTemplate().marshalSendAndReceive(soapServerEndpointUrl, request, message -> {
			try {
				SoapMessage soapMessage = (SoapMessage) message;
				SoapHeader soapHeader = soapMessage.getSoapHeader();

				JAXBContext context = JAXBContext.newInstance(AttributedURIType.class, EndpointReferenceType.class);

				ObjectFactory objectFactory = new ObjectFactory();
				
				AttributedURIType action = objectFactory.createAttributedURIType();

				action.setValue("");
				soapMessage.setSoapAction("");

				AttributedURIType to = objectFactory.createAttributedURIType();

				to.setValue(soapServerEndpointUrl);
				
				AttributedURIType messageId = objectFactory.createAttributedURIType();

				messageId.setValue("1234id");
				
				AttributedURIType replyToURI = objectFactory.createAttributedURIType();

				replyToURI.setValue("replyURL");
				
				EndpointReferenceType replyTo = objectFactory.createEndpointReferenceType();
				
				replyTo.setAddress(replyToURI);
				
				RelatesToType relatesTo = objectFactory.createRelatesToType();
				relatesTo.setRelationshipType("http://www.w3.org/2005/08/addressing/reply");
				relatesTo.setValue("1234relateId");
				
				Marshaller marshaller = context.createMarshaller();
				
				marshaller.marshal(objectFactory.createAction(action), soapHeader.getResult());
				
				
			} catch (Exception e) {
				log.error("soap client error ::: {} ", e);
			}
		});
	}

}
