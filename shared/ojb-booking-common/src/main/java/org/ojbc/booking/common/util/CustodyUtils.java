package org.ojbc.booking.common.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;


public class CustodyUtils {

	/**
	 * Gets age based on birthdate - or returns null if it cannot be determined
	 */
	public static Integer getAge(Date pDob){					
		
		Integer rAgeYears = null;
		
		if(pDob != null){

			LocalDate dobLocalDate = pDob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			rAgeYears = Period.between(dobLocalDate, LocalDate.now()).getYears();			
		}
		
		return rAgeYears;
	}	

}
