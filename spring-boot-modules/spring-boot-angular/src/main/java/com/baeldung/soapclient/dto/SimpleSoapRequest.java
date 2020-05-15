package com.baeldung.soapclient.dto;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimpleSoapRequest", propOrder = { "string1", "string2", "number1" })
@XmlRootElement(name = "SimpleSoapRequest")
public class SimpleSoapRequest {
	
	@XmlElement(name = "string1")
	protected String string1;
	
	@XmlElement(name = "string2")
	protected JAXBElement<String> string2;
	
	@XmlElement(name = "number1")
	protected Integer number1;

}
