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
package org.ojbc.util.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

public final class OJBCDateUtils {
	private static final Log log = LogFactory.getLog( OJBCDateUtils.class );

	public static int returnAgeFromBirthDate(Calendar birthDate)
	{ 
		Calendar today = Calendar.getInstance();  
		int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);  
		if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH)) {
		  age--;  
		} else if (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH)
		    && today.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH)) {
		  age--;  
		}

		return age;
	}

	public static LocalDateTime parseLocalDateTime(String dateTimeString) {
		
		try{
			if (StringUtils.isNotBlank(dateTimeString)){
				if (dateTimeString.length() > 19){
					dateTimeString = dateTimeString.substring(0, 19);
				}
				return LocalDateTime.parse(dateTimeString);
			}
			else{
				log.warn("The dateTimeString is blank");
			}
		}
		catch (DateTimeParseException e){
			log.warn("Failed to parse dateTimeString " + dateTimeString, e);
		}
		
		return null;
	}
	
	/**
	 * @param dateString
	 * @return the parsed LocalDate or null if the dateString is not valid or parsing failure. 
	 */
	public static LocalDate parseLocalDate(String dateString) {
		
		try{
			if (StringUtils.isNotBlank(dateString)){
				return LocalDate.parse(dateString);
			}
			else{
				log.warn("The dateString is blank");
			}
		}
		catch (DateTimeParseException e){
			log.warn("Failed to parse dateString " + dateString, e);
		}
		
		return null;
	}
	
	/**
	 * @param startDate
	 * @param period
	 * @return the startDate + period * year
	 */
	public static Date getEndDate(Date startDate,
			Integer period) {
		
		if (startDate != null && period != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			
			calendar.add(Calendar.YEAR, period);
			Date defaultEndDate = calendar.getTime();
			return defaultEndDate;
		}
		
		return null;
	}

	public static DateTime toDateTime(Date date) {
		return date == null ? null : new DateTime(date);
	}

	public static LocalDate toLocalDate(java.sql.Date date){
		return Optional.ofNullable(date).map(java.sql.Date::toLocalDate).orElse(null);
	}
	
	public static Date toDate(LocalDate localDate){
		return localDate != null ? 
				Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
	}
	
	public static Date toDate(LocalDateTime localDateTime){
		return localDateTime != null ? 
				Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()) : null;
	}
	
	public static LocalDate toLocalDate(Date date){
		return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;  
	}
	public static LocalDateTime toLocalDateTime(Date date){
		return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;  
	}
	public static LocalDateTime toEndLocalDateTime(Date date){
		return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1) : null;  
	}
}
