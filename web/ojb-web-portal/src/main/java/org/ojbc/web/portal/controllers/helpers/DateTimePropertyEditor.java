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
