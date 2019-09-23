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
	public static final String RAPBACK_TOPIC_SUB_TYPE = "{http://ojbc.org/wsn/topics}:person/rapback";
	public static final String RAPBACK_TOPIC_SUB_TYPE_CI = "{http://ojbc.org/wsn/topics}:person/rapback/ci";
	public static final String RAPBACK_TOPIC_SUB_TYPE_CS = "{http://ojbc.org/wsn/topics}:person/rapback/cs";

	//Subscription Category Constants
	public static final String CRIMINAL_JUSTICE_INVESTIGATIVE = "CI";
	public static final String CRIMINAL_JUSTICE_SUPERVISION = "CS";
	public static final String FIREARMS = "F";
	public static final String NON_CRIMINAL_JUSTICE_EMPLOYMENT = "I";
	public static final String CRIMINAL_JUSTICE_EMPLOYMENT = "J";
	public static final String SECURITY_CLEARANCE_INFORMATION_ACT = "S";
	
    public static final String SID_REGEX = "([a-zA-Z]\\d+)?";
    public static final String SSN_REGEX = "(\\d{3}-\\d{2}-\\d{4})?";
    public static final String FBI_ID_REGEX = "^(\\d{1,7}|\\d{1,6}[A-Ha-h]|\\d{1,6}[J-Nj-nP-Zp-z]([1-9]|1[01])|\\d{1,6}[AaC-Fc-fHhJ-Nj-nPRTprtV-Xv-x][A-Ea-e]\\d)?$";
    
    public static final String FBI_ID_PATTERN_ONE_TO_SEVEN_DIGITS = "^\\d{1,7}$";
    public static final String FBI_ID_PATTERN_ONE_TO_SIX_DIGITS_PLUS_ALPHA_AH = "^\\d{1,6}[A-Ha-h]$";
    public static final String FBI_ID_PATTERN_ONE_TO_SIX_DIGITS_PLUS_ALPHA_JZ_1OR2CHK = "^\\d{1,6}[J-Nj-nP-Zp-z]([1-9]|1[01])$";
    public static final String FBI_ID_PATTERN_ONE_TO_SIX_DIGITS_PLUS_2ALPHA_1CHK = "^\\d{1,6}[AaC-Fc-fHhJ-Nj-nPRTprtV-Xv-x][A-Ea-e]\\d$";

    public enum ArrestType{
    	DA("District Attorney"), MUNI("Municipal Court"), OSBI("Criminal History Repository");
    	
    	private String description; 
    	private ArrestType(String description) {
    		this.setDescription(description);
    	}
		public String getDescription() {
			return description;
		}
		private void setDescription(String description) {
			this.description = description;
		}
    }
}
