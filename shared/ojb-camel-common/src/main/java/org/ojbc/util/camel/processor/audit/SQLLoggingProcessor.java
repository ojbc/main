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
