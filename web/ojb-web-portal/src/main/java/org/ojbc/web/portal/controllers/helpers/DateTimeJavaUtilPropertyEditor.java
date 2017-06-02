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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class DateTimeJavaUtilPropertyEditor extends PropertyEditorSupport {
		
	private final Log logger = LogFactory.getLog(this.getClass());
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
	@Override
	public String getAsText() {
		
		Object oDate = getValue();
		
		if(oDate == null){
			return null;
		}
				
		Date d = (Date)getValue();
				
		String sDate = sdf.format(d);
				
		return sDate;
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
	
		Date d = null;
		
		try {
			d = sdf.parse(text);
		} catch (ParseException e) {
			logger.error("Could not parse date: " + text);
		}
				
		setValue(d);		
	}
	
}





