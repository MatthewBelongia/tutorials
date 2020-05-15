package com.baeldung.application.tls;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfiguredRestTemplate {
	
	@Value("${https.trust-store-self-sign}")
	private boolean selfSignTrust;
	
	@Value("${server.ssl.key-store-type}")
	private String keyStoreType;
	
	@Value("${server.ssl.key-store}")
	private String keyStorePath;
	
	@Value("#{'${server.ssl.key-store-password}'.toCharArray()}")
	private char[] keyStorePassword;
	
	@Value("${server.ssl.trust-store-type}")
	private String trustStoreType;
	
	@Value("${server.ssl.trust-store}")
	private String trustStorePath;
	
	@Value("#{'${server.ssl.trust-store-password}'.toCharArray()}")
	private char[] trustStorePassword;
	
	private boolean hostNameVerification = false;
	
	private int timeout=200000;
	
	@Value("${https.callout.connection-pool}")
	private int connectionPool;
	
	public Registry<ConnectionSocketFactory> connSocketFactoryRegistry() throws Exception{
		return RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslConnectionSocketFactory()).build();
	}
	
	private SSLConnectionSocketFactory sslConnectionSocketFactory() throws Exception{
		SSLContext sslContext = SSLContextBuilder.create()
				.loadKeyMaterial(generateKeyStore(keyStoreType,keyStorePath,keyStorePassword),keyStorePassword)
				.loadTrustMaterial(generateKeyStore(trustStoreType,trustStorePath,trustStorePassword),
						selfSignTrust ? new TrustSelfSignedStrategy() : null)
				.build();
		
		return new SSLConnectionSocketFactory(sslContext, hostNameVerification ? null : NoopHostnameVerifier.INSTANCE);
	}
	
	private KeyStore generateKeyStore(String keyStoreType, String keyStorePath, char[] keyStorePassword) throws Exception{
		
		KeyStore keyStore = KeyStore.getInstance(keyStoreType);
		try(InputStream fileStream = new FileInputStream(new File(keyStorePath));){
			keyStore.load(fileStream, keyStorePassword);
		}
		
		return keyStore;
		
	}
	
	private PoolingHttpClientConnectionManager connectionManager() throws Exception {
		
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(connSocketFactoryRegistry());
		connectionManager.setMaxTotal(connectionPool);
		connectionManager.setDefaultMaxPerRoute(connectionPool);
		
		return connectionManager;
	}
	
	private RequestConfig requestConfig() {
		return RequestConfig.custom()
				.setConnectionRequestTimeout(timeout)
				.setConnectTimeout(timeout)
				.setSocketTimeout(timeout)
				.build();
	}
	
	private CloseableHttpClient httpClient() throws Exception{
		return HttpClients.custom()
				.setConnectionManager(connectionManager())
				.setDefaultRequestConfig(requestConfig())
				.build();
	}
	
	@Bean
	@Primary
	public RestTemplate restTemplate() throws Exception{
		
		return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient()));
	}
	

}
