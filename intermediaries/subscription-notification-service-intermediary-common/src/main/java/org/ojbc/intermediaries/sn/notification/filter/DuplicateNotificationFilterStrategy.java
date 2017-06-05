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
package org.ojbc.intermediaries.sn.notification.filter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.ojbc.intermediaries.sn.dao.Notification;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * This filter will look at the notification ID (incident id for example) and subject identifier to determine
 * if a notification has already been sent for this event.  If not, the notification is logged to the database.
 *
 */
public class DuplicateNotificationFilterStrategy implements NotificationFilterStrategy{

	private static final Log log = LogFactory.getLog( DuplicateNotificationFilterStrategy.class );
	
	/**
	 * JDBC template provided by Spring to simplify database interactions.  
	 */
    protected JdbcTemplate jdbcTemplate;

	
	@Override
	public boolean shouldMessageBeFiltered(NotificationRequest request) {

		boolean shouldMessagebeFiltered = false;
		
		//Subject identifier defined by notification request processors
		String subjectIdentifier = request.getDescriptiveSubjectIdentifier();
		
		//This is typically the Incident ID or the Arrest ID
		String notificationId = request.getNotificationEventIdentifier();

		//This is the system that produced the notification
		String notifyingSystem = request.getNotifyingSystemName();
		
		log.debug("Subject Identifier: " + subjectIdentifier + " Notification ID (incident ID): " + notificationId + " Notifying System: " + notifyingSystem);
		
		String queryString = "SELECT * FROM notifications_sent where notificationId =? and notificationSubjectIdentifier=? and notifyingSystem = ?";

		Object[] criteriaArray = new Object[] { notificationId,subjectIdentifier, notifyingSystem};
		
		List<Notification> notificationsSent = this.jdbcTemplate.query(queryString, criteriaArray , new NotificationsSentMapper() );
		
		if (notificationsSent.size() == 0)
		{	
			log.debug("Not a duplicate notification.  Persist notification info to database");
			
			shouldMessagebeFiltered = false;
			
			//insert into database here
			this.jdbcTemplate.update("insert into notifications_sent (notificationId,notificationSubjectIdentifier, notifyingSystem) values (?,?, ?)",
					notificationId, subjectIdentifier, notifyingSystem);
			
		}
		if (notificationsSent.size() == 1)
		{	
			log.debug("Duplicate Notification: " + notificationId);
			shouldMessagebeFiltered = true;
		}
		
		return shouldMessagebeFiltered;
	}

    /**
     * Datasource injected by Spring context
     * @param dataSource
     */
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
	/**
	 * This method will map the subscription result set row to a POJO. It is the
	 * standard way that Spring JDBC template is used.
	 * 
	 * @author yogeshchawla
	 * 
	 */
	private static final class NotificationsSentMapper implements
			RowMapper<Notification> {

		public Notification mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			
			Notification notificationSent = new Notification();
			
			notificationSent.setNotificationId(rs.getString("notificationId"));
			notificationSent.setNotificationSubjectIdentifier(rs.getString("notificationSubjectIdentifier"));
			
			return notificationSent;
		}
	}
}
