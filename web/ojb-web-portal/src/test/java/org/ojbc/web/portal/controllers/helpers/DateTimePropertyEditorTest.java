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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class DateTimePropertyEditorTest {

	private DateTimePropertyEditor unit;
	
	@Before
	public void setup(){
		unit = new DateTimePropertyEditor();
	}

	@Test
	public void setAsTextWheninputIsnull() {
		unit.setAsText(null);
		
		assertThat(unit.getValue(),nullValue());
	}

	@Test
	public void setAsTextWhenEmpty() {
		unit.setAsText("");
		assertThat(unit.getValue(),nullValue());
	}
	
	@Test
	public void setAsTestWithValidDate(){
		unit.setAsText("02/03/2013");
		DateTime expectedValue = (DateTime) unit.getValue();
		assertThat(expectedValue.getMonthOfYear(),is(2));
		assertThat(expectedValue.getYear(),is(2013));
		assertThat(expectedValue.getDayOfMonth(),is(3));
	}
	
	@Test(expected=RuntimeException.class)
	public void setAsTestWithInvalidDateFormat(){
		
		unit.setAsText("02-03-2013");
		assertThat(unit.getValue(),nullValue());
	}

	@Test
	public void setAsTestWithChompSpaces(){
		unit.setAsText("   02/03/2013   ");
		DateTime expectedValue = (DateTime) unit.getValue();
		assertThat(expectedValue.getMonthOfYear(),is(2));
		assertThat(expectedValue.getYear(),is(2013));
		assertThat(expectedValue.getDayOfMonth(),is(3));
	}
	
	
	@Test
	public void getAsTextWhenInputIsNull(){
		unit.setValue(null);
		assertThat(unit.getAsText(), is(""));
	}

	@Test
	public void getAsTextWillChopOffTime(){
		unit.setValue(new DateTime(2001,5,3,10,11,12,222));
		assertThat(unit.getAsText(), is("5/3/2001"));
	}
	
	@Test
	public void setAsTextWithValidDateShortFormatCurrentCentury(){
		unit.setAsText("10/1/13");
		DateTime expectedValue = (DateTime) unit.getValue();
		assertThat(expectedValue.getMonthOfYear(),is(10));
		assertThat(expectedValue.getYear(),is(2013));
		assertThat(expectedValue.getDayOfMonth(),is(1));

	}

	@Test
	public void setAsTextWithValidDateShortFormatPreviousCentury(){
		
		DateTime todayPlus11years = new DateTime();
		todayPlus11years = todayPlus11years.plusYears(11);
		
		String dateWithTwoDigitYear = todayPlus11years.toString("MM/dd/yy");
		
		unit.setAsText(dateWithTwoDigitYear);
		DateTime expectedValue = (DateTime) unit.getValue();
		assertThat(expectedValue.getMonthOfYear(),is(todayPlus11years.getMonthOfYear()));
		assertThat(expectedValue.getDayOfMonth(),is(todayPlus11years.getDayOfMonth()));
		assertThat(expectedValue.getYear(),is(todayPlus11years.minusYears(100).getYear()));

	}

	
	@Test(expected=IllegalArgumentException.class)
	public void setAsTextWithInvalidDate(){
		unit.setAsText("10/1/131");
	}

}
