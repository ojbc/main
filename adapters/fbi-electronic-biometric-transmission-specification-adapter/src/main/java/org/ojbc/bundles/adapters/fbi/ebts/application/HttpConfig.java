package org.ojbc.bundles.adapters.fbi.ebts.application;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class HttpConfig {
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
    
//    @Bean
//    PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
//        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(15, TimeUnit.SECONDS);
//        manager.setMaxTotal(appProperties.getHttpMaxTotalConnections());
//        manager.setDefaultMaxPerRoute(appProperties.getHttpConnectionsPerRoute());
//        manager.setValidateAfterInactivity(appProperties.getHttpValidateAfterInactivity());
//        manager.closeIdleConnections(15, TimeUnit.SECONDS);
//        return manager;
//    }
    
    @Bean("https")
    HttpComponent httpsComponent(SSLContextParameters sslContextParameters) {
     HttpComponent comp = camelContext.getComponent("https", HttpComponent.class);
        comp.setMaxTotalConnections(appProperties.getHttpMaxTotalConnections());
        comp.setConnectionsPerRoute(appProperties.getHttpConnectionsPerRoute());
        comp.setConnectionRequestTimeout(appProperties.getHttpConnectionRequestTimeout());
        comp.setSocketTimeout(appProperties.getHttpSocketTimeout());
        comp.setConnectionTimeToLive(appProperties.getHttpConnectionTimeToLive());
        comp.setSslContextParameters(sslContextParameters);  
        comp.setX509HostnameVerifier(noopHostnameVerifier);
        log.info("Custom HTTPS Component initialized with truststore and keystore: " + 
                sslContextParameters.getTrustManagers().getKeyStore().getResource() + "\t" + 
                sslContextParameters.getKeyManagers().getKeyStore().getResource()); 
        return comp;
    }
    
}    
