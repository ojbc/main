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
package org.ojbc.adapters.analyticaldatastore.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Node;

public class AnalyticalDataStoreUtils {

	private static final Log log = LogFactory.getLog( AnalyticalDataStoreUtils.class );
	
	public static Map<String, Object> retrieveMapOfPersonAttributes(Node personNode) throws Exception{
		return retrieveMapOfPersonAttributes(personNode, "nc", "jxdm40");
	}
	
	public static Map<String, Object> retrieveMapOfPersonAttributes(Node personNode, String ncPrefix, String jxdmPrefix) throws Exception{

		Map<String, Object> arrestee = new HashMap<String, Object>();
		
		String personFirstName=XmlUtils.xPathStringSearch(personNode, ncPrefix + ":PersonName/" + ncPrefix + ":PersonGivenName");
		
		if (StringUtils.isNotBlank(personFirstName))
		{
			log.debug("Arrestee First Name: " + personFirstName);
			arrestee.put("personFirstName", personFirstName);
		}	
		
		String personMiddleName=XmlUtils.xPathStringSearch(personNode, ncPrefix + ":PersonName/" + ncPrefix +":PersonMiddleName");
		
		if (StringUtils.isNotBlank(personMiddleName))
		{
			log.debug("Arrestee Middle Name: " + personMiddleName);
			arrestee.put("personMiddleName", personMiddleName);
		}	
				
		String personLastName=XmlUtils.xPathStringSearch(personNode, ncPrefix + ":PersonName/" + ncPrefix + ":PersonSurName");
		
		if (StringUtils.isNotBlank(personLastName))
		{
			log.debug("Arrestee Last Name: " + personLastName);
			arrestee.put("personLastName", personLastName);
		}	
								
		String personDateOfBirth=XmlUtils.xPathStringSearch(personNode,ncPrefix + ":PersonBirthDate/"+ ncPrefix + ":Date");
		
		if (StringUtils.isNotBlank(personDateOfBirth))
		{
			log.debug("Arrestee DOB: " + personDateOfBirth);
			arrestee.put("personDateOfBirth", personDateOfBirth);
		}	
				
		String personRace=XmlUtils.xPathStringSearch(personNode, ncPrefix + ":PersonEthnicityText");
		
		if (StringUtils.isNotBlank(personRace))
		{
			arrestee.put("personRace", personRace);
		}	
				
		String personSex=XmlUtils.xPathStringSearch(personNode, ncPrefix + ":PersonSexCode");
		
		if (StringUtils.isBlank(personSex))
		{
			personSex = XmlUtils.xPathStringSearch(personNode, ncPrefix + ":PersonSexText");
		}
		
		if (StringUtils.isNotBlank(personSex)){
			log.debug("Arrestee Sex Code: " + personSex);
			arrestee.put("personSex", personSex);
		}
				
		String personBiometricID=XmlUtils.xPathStringSearch(personNode, jxdmPrefix+":PersonAugmentation/"+jxdmPrefix+
				":PersonStateFingerprintIdentification/"+ncPrefix +":IdentificationID");
		
		if (StringUtils.isNotBlank(personBiometricID))
		{
			log.debug("Arrestee SID: " + personBiometricID);
			arrestee.put("personBiometricID", personBiometricID);
		}	
		
		return arrestee;
	}

}
