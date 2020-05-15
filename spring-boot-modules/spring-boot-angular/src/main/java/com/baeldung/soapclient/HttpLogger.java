package com.baeldung.soapclient;

import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.transform.TransformerObjectSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpLogger extends TransformerObjectSupport {

	private static String NEW_LINE = System.getProperty("line.separator");

	private HttpLogger() {
	}

	public static void logMessage(String id, WebServiceMessage webServiceMessage) {
		try {
			ByteArrayTransportOutputStream byteArrayTransportOutputStream = new ByteArrayTransportOutputStream();

			webServiceMessage.writeTo(byteArrayTransportOutputStream);

			String httpMessage = new String(byteArrayTransportOutputStream.toByteArray());
			log.info(NEW_LINE + "::::::::::::::::" + NEW_LINE + id + NEW_LINE + "::::::::::::::::" + httpMessage);

		} catch (Exception e) {
			log.error("error occured ::::::: ", e);
		}
	}

}
