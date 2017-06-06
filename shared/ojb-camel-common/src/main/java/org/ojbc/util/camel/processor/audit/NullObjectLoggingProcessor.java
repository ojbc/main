/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
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
