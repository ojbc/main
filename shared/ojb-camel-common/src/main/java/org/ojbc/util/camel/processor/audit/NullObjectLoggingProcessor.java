package org.ojbc.util.camel.processor.audit;

import java.net.UnknownHostException;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Camel processor bean that logs exchange information to the installed log mechanism (typically, console or System out logging).
 *
 */
public class NullObjectLoggingProcessor extends AbstractLoggingProcessor {

    private static final Log log = LogFactory.getLog(NullObjectLoggingProcessor.class);

    public void logExchange(Exchange e) throws UnknownHostException, Exception {

        log.info("Audit logging exchange to console");

        LogInfo logInfo = extractLogInfoFromExchange(e);

        log.info("origin=" + logInfo.origin);
        log.info("destination=" + logInfo.destination);
        log.info("federationId=" + logInfo.federationId);
        log.info("employerName=" + logInfo.employerName);
        log.info("employerSubUnitName=" + logInfo.employerSubUnitName);
        log.info("userLastName=" + logInfo.userLastName);
        log.info("userFirstName=" + logInfo.userFirstName);
        log.info("idp=" + logInfo.idp);
        log.info("hostAddress=" + logInfo.hostAddress);
        log.info("camelContextId=" + logInfo.camelContextId);
        //log.info("soapMessage=" + logInfo.soapMessage);

    }

}
