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
package org.ojbc.web.portal.controllers.helpers;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class DateTimePropertyEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {

		if (StringUtils.isBlank(text))
		{
			return;
		}	
		
		try {
			String[] tokens = text.trim().split("/");
			
			//We defensively check for a two digit year date here even though the UI should never let it through
			//If date entered is of the format mm/dd/yy, and yy > current year + 10, then assume the prior century
			String yearAsString = tokens[2];
			
			if ((yearAsString.length() != 2) && (yearAsString.length() != 4))
			{
				throw new IllegalArgumentException("Unable to parse date");
			}	

			DateTime currentDate = new DateTime();
			
			if (yearAsString.length() == 2)
			{
				int lastTwoDigitsOfCurrentYear = currentDate.getYearOfCentury();
				int lastTwoDigitsOfEnteredDate = Integer.valueOf(yearAsString);
				
				if (lastTwoDigitsOfEnteredDate > lastTwoDigitsOfCurrentYear + 10)
				{
					yearAsString = String.valueOf(currentDate.minusYears(100).getCenturyOfEra()) +  yearAsString;
				}	
				else
				{	
					yearAsString = String.valueOf(currentDate.getCenturyOfEra()) +  yearAsString;
				}	
				
			}	
			
			this.setValue(new DateTime(Integer.valueOf(yearAsString), Integer.valueOf(tokens[0]), Integer
			        .valueOf(tokens[1]), 0, 0, 0, 0));
		} catch (RuntimeException ex) {
			throw new IllegalArgumentException("Unable to parse date");
		}
	}

	@Override
	public String getAsText() {
		DateTime value = (DateTime) this.getValue();
		if (value == null) {
			return "";
		}
		return value.getMonthOfYear() + "/" + value.getDayOfMonth() + "/" + value.getYear();
	}
}
