package org.ojbc.web.portal.controllers.helpers;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;



public class DateTimeJavaUtilPropertyEditor extends PropertyEditorSupport {
		
	private Logger logger = Logger.getLogger(DateTimeJavaUtilPropertyEditor.class.getName());
	
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
			logger.severe("Could not parse date: " + text);
		}
				
		setValue(d);		
	}
	
}





