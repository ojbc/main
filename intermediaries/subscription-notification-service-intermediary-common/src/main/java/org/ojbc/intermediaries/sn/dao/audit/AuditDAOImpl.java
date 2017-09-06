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
package org.ojbc.intermediaries.sn.dao.audit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.notification.EmailNotification;
import org.ojbc.intermediaries.sn.notification.RapbackTriggeringEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

public class AuditDAOImpl implements AuditDAO {

	private static final Log log = LogFactory.getLog(AuditDAOImpl.class);
	
    @Autowired
	private JdbcTemplate jdbcTemplateAudit;
	
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplateAudit = new JdbcTemplate(dataSource);
    }
	
	@Override
	public Integer saveNotificationLogEntry(EmailNotification emailNotification) {

		log.debug("Inserting row into Notifications Sent table");
		
        final String notificationSentInsertStatement="INSERT into NOTIFICATIONS_SENT (SUBSCRIPTION_TYPE, TOPIC, SUBSCRIPTION_IDENTIFIER, SUBSCRIPTION_OWNER_AGENCY, SUBSCRIPTION_OWNER_AGENCY_TYPE, SUBSCRIPTION_OWNER_EMAIL_ADDRESS, SUBSCRIPTION_OWNER, SUBSCRIBING_SYSTEM_IDENTIFIER) values (?,?,?,?,?,?,?,?)";
        
        //TODO: determine these values
		String subscriptionType = "";
		String subscriptionOwnerAgencyType = "";
		
		Subscription subscription = emailNotification.getSubscription();
		
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplateAudit.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(notificationSentInsertStatement, new String[] {"NOTIFICATIONS_SENT_ID"});
        	            ps.setString(1, subscriptionType);
        	            ps.setString(2, subscription.getTopic());
        	            ps.setInt(3, (int) (long) subscription.getId());
        	            ps.setString(4, subscription.getOri());
        	            ps.setString(5, subscriptionOwnerAgencyType);
        	            ps.setString(6, subscription.getSubscriptionOwnerEmailAddress());
        	            ps.setString(7, subscription.getSubscriptionOwner());
        	            ps.setString(8, subscription.getSubscribingSystemIdentifier());
        	            return ps;
        	        }
        	    },
        	    keyHolder);
		
        Integer notificationSentID = keyHolder.getKey().intValue();		
        
        saveTriggeringEventEntries(notificationSentID, emailNotification.getTriggeringEvents());
        
        saveNotificationMechanismEntries(notificationSentID, emailNotification);
        
        
        return notificationSentID;
	}

	private void saveTriggeringEventEntries(Integer notificationSentID,
			List<RapbackTriggeringEvent> triggeringEvents) {
		
		if (triggeringEvents != null && triggeringEvents.size() > 0)
		{	
			for (RapbackTriggeringEvent rapbackTriggeringEvent : triggeringEvents)
			{
				String propertyName = "RAPBACK_TRIGERRING_EVENT_CODE";
				String triggeringEventCode = rapbackTriggeringEvent.getTriggeringEventCode();
				
				saveNotificationProperty(notificationSentID, propertyName, triggeringEventCode);
			}	
			
		}
	}

	private Integer saveNotificationProperty(Integer notificationSentID, String propertyName, String propertyValue) {
		log.debug("Inserting row into Notifications Properties table");
		
        final String insertStatement="INSERT into NOTIFICATION_PROPERTIES (NOTIFICATIONS_SENT_ID, PROPERTY_NAME,PROPERTY_VALUE) values (?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplateAudit.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(insertStatement, new String[] {"NOTIFICATION_PROPERTIES_ID"});
        	            ps.setInt(1, notificationSentID);
        	            ps.setString(2, propertyName);
        	            ps.setString(3, propertyValue);
        	            return ps;
        	        }
        	    },
        	    keyHolder);
		
        return keyHolder.getKey().intValue();	
		
	}
	
	private void saveNotificationMechanismEntries(
			Integer notificationSentID, EmailNotification emailNotification) {
		//TO Addresses
		for(String emailAddress : emailNotification.getToAddresseeSet())
		{
			saveNotificationMechanismEntry(notificationSentID,"email", emailAddress, "to");
		}	
		
		//CC Addresses
		for(String emailAddress : emailNotification.getCcAddresseeSet())
		{
			saveNotificationMechanismEntry(notificationSentID,"email", emailAddress, "cc");
		}	
		
		//BCC Addresses
		for(String emailAddress : emailNotification.getBccAddresseeSet())
		{
			saveNotificationMechanismEntry(notificationSentID,"email", emailAddress, "bcc");
		}	
		
	}

	private Integer saveNotificationMechanismEntry(Integer notificationSentID, String notificationMechanism, String emailAddress, String notificationRecipientType) {
		log.debug("Inserting row into Notifications Sent table");
		
        final String insertStatement="INSERT into NOTIFICATION_MECHANISM (NOTIFICATIONS_SENT_ID, NOTIFICATION_MECHANSIM,NOTIFICATION_ADDRESS,NOTIFICATION_RECIPIENT_TYPE) values (?,?,?,?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplateAudit.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(insertStatement, new String[] {"NOTIFICATION_MECHANISM_ID"});
        	            ps.setInt(1, notificationSentID);
        	            ps.setString(2, notificationMechanism);
        	            ps.setString(3, emailAddress);
        	            ps.setString(4, notificationRecipientType);
        	            return ps;
        	        }
        	    },
        	    keyHolder);
		
        return keyHolder.getKey().intValue();	
		
	}
	
	@Override
	@Transactional
	public Integer deleteNotificationLogEntry(Integer notificationSentId) {
		String sql = "Delete from NOTIFICATION_PROPERTIES where NOTIFICATIONS_SENT_ID=" + notificationSentId;
		jdbcTemplateAudit.update(sql);	

		sql = "Delete from NOTIFICATION_MECHANISM where NOTIFICATIONS_SENT_ID=" + notificationSentId;
		jdbcTemplateAudit.update(sql);	

		sql = "Delete from NOTIFICATIONS_SENT where NOTIFICATIONS_SENT_ID=" + notificationSentId;
		Integer rowsDeleted = jdbcTemplateAudit.update(sql);	
		
		return rowsDeleted;
		
	}	
	
	@Override
	public NotificationsSent retrieveNotificationSentById(
			Integer notificationSentId) {

		String sql = "SELECT * from NOTIFICATIONS_SENT "
				+ "WHERE NOTIFICATIONS_SENT_ID=?  ";
		
		List<NotificationsSent> notificationsSent = 
				jdbcTemplateAudit.query(sql, new NotificationSentMapper(), 
						notificationSentId);
		
		sql = "SELECT * from NOTIFICATION_PROPERTIES "
				+ "WHERE NOTIFICATIONS_SENT_ID=?  ";
		
		List<NotificationProperties> notificationProperties = 
				jdbcTemplateAudit.query(sql, new NotificationPropertiesMapper(), 
						notificationSentId);

		sql = "SELECT * from NOTIFICATION_MECHANISM "
				+ "WHERE NOTIFICATIONS_SENT_ID=?  ";
		
		List<NotificationMechanism> notificationsMechanisms = 
				jdbcTemplateAudit.query(sql, new NotificationMechanismMapper(), 
						notificationSentId);

		if (notificationsSent.get(0) != null)
		{
			notificationsSent.get(0).setNotificationMechanisms(notificationsMechanisms);
			notificationsSent.get(0).setNotificationProperties(notificationProperties);
			
		}	
		
		return DataAccessUtils.singleResult(notificationsSent);
	}
	
	public JdbcTemplate getJdbcTemplateAudit() {
		return jdbcTemplateAudit;
	}

	public void setJdbcTemplateAudit(JdbcTemplate jdbcTemplateAudit) {
		this.jdbcTemplateAudit = jdbcTemplateAudit;
	}


}
