package org.ojbc.bundles.adapters.fbi.ebts.application;

import org.apache.camel.CamelContext;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
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
    NoopHostnameVerifier noopHostnameVerifier() {
        return NoopHostnameVerifier.INSTANCE;
    }
    
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
        
}    
