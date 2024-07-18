package org.ojbc.intermediaries.sn;

import javax.annotation.Resource;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailEncryptionService extends RouteBuilder{
	
	@Resource
	SubscriptionNotificationServiceProperties appProperties;
	
    @Override
    public void configure() throws Exception {
        from("direct:encryptBody").routeId("encryptionRoute")
        .log("In encryption route.")
        .filter()
           .simple(appProperties.getEncryptEmailBody() + "== 'true'")
            	.marshal()
            	.pgp(appProperties.getPgpPublicKeyFileName(), appProperties.getPgpUserName())
            	.log("Email body encrypted and marshalled.");
    }
}
