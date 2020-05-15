package com.baeldung.soapclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender.RemoveSoapHeadersInterceptor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SoapClientConfig {

	private String marshallerPackagesToScan = "com.baeldung.springsoap.gen";
	private boolean sslEnabled = false;
	private boolean hostnameVerification = false;
	private boolean trustSelfSigned = true;

	private String unmarshallerPackagesToScan = "com.baeldung.springsoap.gen";
	private String trustStore="truststore.jks";
	private char[] trustStorePassword="123456".toCharArray();
	private String trustStoreType="JKS";

	private String keyStore="KeyStore.jks";
	private char[] keyStorePassword="123456".toCharArray();
	private String keyStoreType="JKS";
	private String keyStoreAlias="bmc";

	@Primary
	@Bean
	public SoapClient soapClient() throws Exception {

		SoapClient soapClient = new SoapClient();
		soapClient.setMarshaller(marshaller());
		soapClient.setUnmarshaller(marshaller());
		soapClient.setMessageFactory(messageFactory());
		soapClient.setMessageSender(httpMessageSender());
		soapClient.setInterceptors(new ClientInterceptor[] { new LoggingInterceptor() });
		return soapClient;

	}

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		log.info("marshallerPackages ::::");
		marshaller.setPackagesToScan(marshallerPackagesToScan.split(","));
		return marshaller;
	}

	@Bean
	public SaajSoapMessageFactory messageFactory() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.setSoapVersion(SoapVersion.SOAP_12);
		return messageFactory;
	}

	@Bean
	public HttpComponentsMessageSender httpMessageSender() throws Exception {

		HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
		httpComponentsMessageSender.setHttpClient(httpClient());
		return httpComponentsMessageSender;
	}

	private HttpClient httpClient() throws Exception {
		if (sslEnabled) {
			return HttpClientBuilder.create().setSSLSocketFactory(sslConnectionSocketFactory())
					.addInterceptorFirst(new RemoveSoapHeadersInterceptor()).build();
		} else {
			return HttpClientBuilder.create().addInterceptorFirst(new RemoveSoapHeadersInterceptor()).build();
		}
	}

	private SSLConnectionSocketFactory sslConnectionSocketFactory() throws Exception {
		return new SSLConnectionSocketFactory(sslContext(),
				hostnameVerification ? null : NoopHostnameVerifier.INSTANCE);
	}

	private SSLContext sslContext() throws Exception {
		return SSLContextBuilder.create()
				.loadKeyMaterial(generateKeyStore(keyStoreType, keyStore, keyStorePassword), keyStorePassword)
				.loadTrustMaterial(generateKeyStore(trustStoreType, trustStore, trustStorePassword),
						trustSelfSigned ? new TrustSelfSignedStrategy() : null)
				.build();
	}

	private KeyStore generateKeyStore(String storeType, String storePath, char[] storePassword) throws Exception {
		KeyStore keyStore = KeyStore.getInstance(storeType);
		try (InputStream fileStream = new FileInputStream(new File(storePath));) {
			log.debug("{}", fileStream);
			keyStore.load(fileStream, storePassword);
		}
		return keyStore;

	}
}
