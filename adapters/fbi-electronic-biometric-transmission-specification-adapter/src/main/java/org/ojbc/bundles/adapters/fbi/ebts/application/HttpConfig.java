package org.ojbc.bundles.adapters.fbi.ebts.application;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.camel.CamelContext;
import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {
    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(HttpConfig.class);
    
    @Autowired
    AppProperties appProperties; 
    @Autowired
    private NoopHostnameVerifier noopHostnameVerifier;
    @Autowired
    private CamelContext camelContext;
    
    @Value("${fbiEbtsAdapter.truststoreLocation}")
    private String truststoreLocation; 
    @Value("${fbiEbtsAdapter.truststorePassword}")
    private String truststorePassword; 
    @Value("${fbiEbtsAdapter.fbi.keyPassword}")
    private String fbiKeyPassword; 
    @Value("${fbiEbtsAdapter.fbi.keystoreLocation}")
    private String fbiKeystoreLocation; 
    @Value("${fbiEbtsAdapter.fbi.keystorePassword}")
    private String fbiKeystorePassword; 
    
    @Bean
    SSLContextParameters sslContextParameters(AppProperties props) {
        KeyStoreParameters trustStore = new KeyStoreParameters();
        trustStore.setResource(truststoreLocation);
        trustStore.setPassword(truststorePassword);

        TrustManagersParameters trustManagers = new TrustManagersParameters();
        trustManagers.setKeyStore(trustStore);

        KeyStoreParameters keyStore = new KeyStoreParameters();
        keyStore.setResource(fbiKeystoreLocation);
        keyStore.setPassword(fbiKeystorePassword);

        KeyManagersParameters keyManagers = new KeyManagersParameters();
        keyManagers.setKeyStore(keyStore);
        keyManagers.setKeyPassword(fbiKeyPassword);

        SSLContextParameters sslContextParameters = new SSLContextParameters();
        sslContextParameters.setTrustManagers(trustManagers);
        sslContextParameters.setKeyManagers(keyManagers);

        return sslContextParameters;
    }
    
    @Bean
    SSLContext customSslContext(SSLContextParameters sslContextParameters) throws Exception {
        // Load custom trust store and key store
        return sslContextParameters.createSSLContext(camelContext);
    }

    @Bean
    PoolingHttpClientConnectionManager customConnectionManager(SSLContext customSslContext) {
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", new SSLConnectionSocketFactory(customSslContext, noopHostnameVerifier))
            .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(appProperties.getHttpMaxTotalConnections());
        connectionManager.setDefaultMaxPerRoute(appProperties.getHttpConnectionsPerRoute());
        connectionManager.setValidateAfterInactivity(appProperties.getHttpValidateAfterInactivity());
        return connectionManager;
    }    
    
    @Bean("https")
    HttpComponent httpsComponent(PoolingHttpClientConnectionManager customConnectionManager) {
        
        HttpClientConfigurer configurer = (HttpClientBuilder clientBuilder) -> {
            clientBuilder
                // use your pool manager
                .setConnectionManager(customConnectionManager)
                .setConnectionTimeToLive(appProperties.getHttpConnectionTimeToLive(), TimeUnit.MILLISECONDS)
                // eviction
                .evictIdleConnections(appProperties.getHttpIdleConnectionTimeout(), TimeUnit.SECONDS)
                .evictExpiredConnections()
                // request config
                .setDefaultRequestConfig(RequestConfig.custom()
                    .setConnectionRequestTimeout(appProperties.getHttpConnectionRequestTimeout())
                    .setConnectTimeout(appProperties.getHttpConnectionTimeout())
                    .setSocketTimeout(appProperties.getHttpSocketTimeout())
                    .build()
                );
        };
        
        HttpComponent comp = camelContext.getComponent("https", HttpComponent.class);
        comp.setHttpClientConfigurer(configurer);
        return comp;
    }
    
}    
