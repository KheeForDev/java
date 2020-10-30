package com.khee.dev.component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.khee.dev.interceptor.RequestResponseLoggingInterceptor;

@Component
public class AppClientRunner implements CommandLineRunner {

	@Value("${client.ssl.key-store}")
	private Resource keyStore;

	@Value("${client.ssl.key-password}")
	private String keyPassword;
	
	@Value("${client.ssl.key-store-password}")
	private String keyStorePassword;

	@Value("${client.ssl.trust-store}")
	private Resource trustStore;

	@Value("${client.ssl.trust-store-password}")
	private String trustStorePassword;

	@Override
	public void run(String... args) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(constructSSLContext());

		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		if (CollectionUtils.isEmpty(interceptors)) {
			interceptors = new ArrayList<>();
		}
		interceptors.add(new RequestResponseLoggingInterceptor());
		restTemplate.setInterceptors(interceptors);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.ALL));
		headers.add("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

		try {
			restTemplate.exchange("https://localhost:7171/server/auth", HttpMethod.GET, null, String.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.exit(0);
	}

	private HttpComponentsClientHttpRequestFactory constructSSLContext() {
		HttpComponentsClientHttpRequestFactory factory = null;

		try {
			if (keyStore.exists() && trustStore.exists()) {
				KeyStore ks = KeyStore.getInstance("JKS");

				FileInputStream fis = new FileInputStream(keyStore.getFile());
				ks.load(fis, keyPassword.toCharArray());
				SSLContext sslContext = new SSLContextBuilder().loadKeyMaterial(ks, keyStorePassword.toCharArray())
						.loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();

				SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
				HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
				factory = new HttpComponentsClientHttpRequestFactory(httpClient);
			} else {
				System.out.println("keystore and/or truststore jks file does not exist");
			}
		} catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException
				| KeyManagementException | UnrecoverableKeyException e) {
			System.out.println(e.getMessage());
		}

		return factory;
	}
}
