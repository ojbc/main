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
package org.ojbc.util.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SubscriptionCategoryCode {
    /**
     * CRIMINAL_JUSTICE_INVESTIGATIVE 
     */
	CI, 
	/**
	 * CRIMINAL_JUSTICE_SUPERVISION
	 */
	CS,
	/**
	 * FIREARMS
	 */
	F,
	/**
	 * NON_CRIMINAL_JUSTICE_EMPLOYMENT
	 */
	I, 
	/**
	 * CRIMINAL_JUSTICE_EMPLOYMENT
	 */
	J,
	/**
	 * SECURITY_CLEARANCE_INFORMATION_ACT
	 */
	S; 
	
	public static List<String> getCivilCodes(){
		return Arrays.asList(F, J, I, S).stream()
				.map(SubscriptionCategoryCode::name)
				.collect(Collectors.toList());
	}
	
	public static boolean isCivilCategoryCode(String code){
		return getCivilCodes().contains(code);
	}
	public static boolean isCriminalCategoryCode(String code){
		return Arrays.asList(CI.name(), CS.name()).contains(code);
	}
}
