package org.ojbc.util.helper;

import java.util.Calendar;

public class OJBCDateUtils {

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
	
}
