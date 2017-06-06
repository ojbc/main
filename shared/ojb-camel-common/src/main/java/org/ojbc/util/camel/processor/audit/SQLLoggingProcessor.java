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

import java.sql.Types;

import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * A Camel processor bean that logs exchange information to a database.
 *
 */
public class SQLLoggingProcessor extends AbstractLoggingProcessor {

    private static final Log log = LogFactory.getLog(SQLLoggingProcessor.class);

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void logExchange(Exchange e) throws Exception {

        log.info("logging exchange to database");

        LogInfo logInfo = extractLogInfoFromExchange(e);

        // log.info("origin=" + logInfo.origin);
        // log.info("destination=" + logInfo.destination);
        // log.info("federationId=" + logInfo.federationId);
        // log.info("idp=" + logInfo.idp);
        // log.info("hostAddress=" + logInfo.hostAddress);
        // log.info("camelContextId=" + logInfo.camelContextId);
        // log.info("soapMessage=" + logInfo.soapMessage);

        writeLogInfoToDatabase(logInfo);

    }

    private void writeLogInfoToDatabase(LogInfo logInfo) {

        String sql = "insert into AuditLog (origin,destination,replyTo,messageID,federationID,employerName,employerSubUnitName,userLastName,userFirstName," +
            "identityProviderID,hostAddress,camelContextID,osgiBundleName,osgiBundleVersion,osgiBundleDescription,soapMessage) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int[] types = new int[] {
            Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.CLOB
        };

        Object[] values = new Object[] {
            logInfo.origin, logInfo.destination, logInfo.replyTo, logInfo.messageId, logInfo.federationId, logInfo.employerName, logInfo.employerSubUnitName,
            logInfo.userLastName, logInfo.userFirstName, logInfo.idp, logInfo.hostAddress, logInfo.camelContextId, logInfo.bundleName,
            logInfo.bundleVersion, logInfo.bundleBundleName, logInfo.soapMessage
        };

        jdbcTemplate.update(sql, values, types);

    }

    JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}
