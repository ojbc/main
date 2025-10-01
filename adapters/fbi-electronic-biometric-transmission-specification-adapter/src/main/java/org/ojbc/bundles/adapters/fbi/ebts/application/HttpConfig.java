package org.ojbc.bundles.adapters.fbi.ebts.application;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class HttpConfig {
    
    @Autowired
    AppProperties appProperties; 
    @Autowired
    private SSLContextParameters sslContextParameters;
    @Autowired
    private NoopHostnameVerifier noopHostnameVerifier;
    @Autowired
    private CamelContext camelContext;
    
    
    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(15, TimeUnit.SECONDS);
        manager.setMaxTotal(appProperties.getHttpMaxTotalConnections());
        manager.setDefaultMaxPerRoute(appProperties.getHttpConnectionsPerRoute());
        manager.setValidateAfterInactivity(appProperties.getHttpValidateAfterInactivity());
        return manager;
    }
    
    @Bean("https")
    HttpComponent httpComponent(PoolingHttpClientConnectionManager manager) {
        
        // Configure the builder directly
        HttpClientConfigurer configurer = (HttpClientBuilder clientBuilder) -> {
            try {
                clientBuilder
                    .setConnectionManager(manager)
                    .setSSLContext(sslContextParameters.createSSLContext(camelContext))
                    .setSSLHostnameVerifier(noopHostnameVerifier)
                    .setKeepAliveStrategy((response, context) -> {
                        return appProperties.getHttpConnectionTimeToLive();
                    })
                    .evictIdleConnections(appProperties.getHttpIdleConnectionTimeout(), TimeUnit.SECONDS)
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectionRequestTimeout(appProperties.getHttpConnectionRequestTimeout())
                            .setConnectTimeout(appProperties.getHttpConnectionTimeout())
                            .setSocketTimeout(appProperties.getHttpSocketTimeout())
                            .build());
            } catch (GeneralSecurityException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        };

        HttpComponent comp = new HttpComponent();
        comp.setHttpClientConfigurer(configurer);
        return comp;
    }

    @Scheduled(fixedDelay = 15000)
    public void evictIdleConnections(PoolingHttpClientConnectionManager manager) {
        manager.closeExpiredConnections();
        manager.closeIdleConnections(appProperties.getHttpIdleConnectionTimeout(), TimeUnit.SECONDS);
    }
}
