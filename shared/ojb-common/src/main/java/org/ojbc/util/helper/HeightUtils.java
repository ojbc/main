package org.ojbc.util.helper;

public class HeightUtils {

	public static String returnFeetFromInches(int heightInInches)
	{
		int feet = heightInInches / 12;
		
		return String.valueOf(feet);
	}
	
	public static String returnRemainingInchesFromInches(int heightInInches)
	{
		int remainingInches = heightInInches % 12;
		
		String returnValue = String.format("%02d", remainingInches);
		
		return returnValue;
	}
}
