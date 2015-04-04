package org.ojbc.ws.security;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;

import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.contrib.ssl.AuthSSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;

public class OJBHttpClientConfigurer implements HttpClientConfigurer {
	
	private String keystoreLocation;
	
	/**
	 * The keystore and private key password must match
	 */
	private String keystorePassword;
	
	private String truststoreLocation;
	private String truststorePassword;
	
	@Override
	public void configureHttpClient(HttpClient client) {
		// register the customer SSLFactory
		Protocol authhttps = null;
		try {
			authhttps = new Protocol(
					"https",
					new AuthSSLProtocolSocketFactory(
							new URL(
									"file:" + keystoreLocation),
							keystorePassword,
							new URL(
									"file:" + truststoreLocation),
							truststorePassword), 443);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		Protocol.registerProtocol("https", authhttps);

	}

	public String getKeystoreLocation() {
		return keystoreLocation;
	}

	public void setKeystoreLocation(String keystoreLocation) {
		this.keystoreLocation = keystoreLocation;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public String getTruststoreLocation() {
		return truststoreLocation;
	}

	public void setTruststoreLocation(String truststoreLocation) {
		this.truststoreLocation = truststoreLocation;
	}

	public String getTruststorePassword() {
		return truststorePassword;
	}

	public void setTruststorePassword(String truststorePassword) {
		this.truststorePassword = truststorePassword;
	}

}
