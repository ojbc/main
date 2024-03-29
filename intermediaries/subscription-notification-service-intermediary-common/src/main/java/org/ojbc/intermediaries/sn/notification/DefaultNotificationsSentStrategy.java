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
package org.ojbc.intermediaries.sn.notification;

import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DefaultNotificationsSentStrategy implements NotificationsSentStrategy{

	private LocalDateTime lastNotificationSentTimestamp;
	
	//Set default value to 24 hours
	private Integer maxHoursBetweenNotifications = 24;
	
	//Set default value of 10
	private Integer minAmountOfNotificationsSent = 10;
	
	private JdbcTemplate jdbcTemplateAudit;
	
	@Override
	public void updateNotificationSentTimestamp() {
		
		setLastNotificationSentTimestamp(LocalDateTime.now());
	}
	
	@Override
	public boolean hasNotificationBeenSent() {
		boolean hasNotificationBeenSent = false;
		
		if (lastNotificationSentTimestamp == null)
		{
			return false;
		}	
		
		LocalDateTime now = LocalDateTime.now();
		
		if (lastNotificationSentTimestamp.isAfter(now.minusHours(maxHoursBetweenNotifications)))
		{
			hasNotificationBeenSent = true;
		}		
		
		return hasNotificationBeenSent;
	}
	
	@Override
	public boolean hasNotifcationAmountBeenSent() { 
		boolean hasNotificationAmountBeenSent = false;
		
	    String sql = "select count(*) from rapback_enhanced_auditlog.notifications_sent"
				+ " where DATEDIFF(s.last_match_date, NOW()) < -1;";
	    
	    Integer notificationCount = this.jdbcTemplateAudit.queryForObject(sql, Integer.class);
	    
	    if(notificationCount > minAmountOfNotificationsSent) {
	    	hasNotificationAmountBeenSent = true;
	    }
		
		return hasNotificationAmountBeenSent;
		
	}

	public LocalDateTime getLastNotificationSentTimestamp() {
		return lastNotificationSentTimestamp;
	}

	public void setLastNotificationSentTimestamp(
			LocalDateTime lastNotificationSentTimestamp) {
		this.lastNotificationSentTimestamp = lastNotificationSentTimestamp;
	}

	public Integer getMaxHoursBetweenNotifications() {
		return maxHoursBetweenNotifications;
	}

	public void setMaxHoursBetweenNotifications(Integer maxHoursBetweenNotifications) {
		this.maxHoursBetweenNotifications = maxHoursBetweenNotifications;
	}
	
	public Integer getMinAmountOfNotificationsSent() {
		return minAmountOfNotificationsSent;
	}

	public void setMinAmountOfNotificationsSent(Integer minAmountOfNotificationsSent) {
		this.minAmountOfNotificationsSent = minAmountOfNotificationsSent;
	}

	 public void setDataSourceAudit(DataSource dataSourceAudit) {
	        this.jdbcTemplateAudit = new JdbcTemplate(dataSourceAudit);
	    }
}
