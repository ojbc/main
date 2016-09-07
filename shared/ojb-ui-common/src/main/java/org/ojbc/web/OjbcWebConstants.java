package org.ojbc.web;

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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
public class OjbcWebConstants {
	public static final String TOPIC_PERSON_ARREST = "{http://ojbc.org/wsn/topics}:person/arrest";
	public static final String CIVIL_SUBSCRIPTION_REASON_CODE="I";
	
    public static final String SID_REGEX = "([a-zA-Z]\\d+)?";
    public static final String SSN_REGEX = "(\\d{3}-\\d{2}-\\d{4})?";
    public static final String FBI_ID_REGEX = "^(\\d{1,7}|\\d{1,6}[A-Ha-h]|\\d{1,6}[J-Nj-nP-Zp-z]([1-9]|1[01])|\\d{1,6}[AaC-Fc-fHhJ-Nj-nPRTprtV-Xv-x][A-Ea-e]\\d)?$";
    
    public static final String FBI_ID_PATTERN_ONE_TO_SEVEN_DIGITS = "^\\d{1,7}$";
    public static final String FBI_ID_PATTERN_ONE_TO_SIX_DIGITS_PLUS_ALPHA_AH = "^\\d{1,6}[A-Ha-h]$";
    public static final String FBI_ID_PATTERN_ONE_TO_SIX_DIGITS_PLUS_ALPHA_JZ_1OR2CHK = "^\\d{1,6}[J-Nj-nP-Zp-z]([1-9]|1[01])$";
    public static final String FBI_ID_PATTERN_ONE_TO_SIX_DIGITS_PLUS_2ALPHA_1CHK = "^\\d{1,6}[AaC-Fc-fHhJ-Nj-nPRTprtV-Xv-x][A-Ea-e]\\d$";


}
