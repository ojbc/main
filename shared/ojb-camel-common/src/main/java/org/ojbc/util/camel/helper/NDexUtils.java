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
package org.ojbc.util.camel.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.ndex.util.UniqueIDGenerator;

public class NDexUtils {

	private static final Log log = LogFactory.getLog(NDexUtils.class);
	
	/*
	 * Inject class that will generated unique N-DEx sequence number 0 to 9999 
	 */
	private UniqueIDGenerator uniqueIDGenerator;

	public String createNDExMessage(Exchange ex, @Header(value = "CamelFileName")String fileName, @Header(value = "submittingAgencyName")String submittingAgencyName ) throws Exception {
		
		String lexsPayload = (String)ex.getIn().getBody();
		
		StringBuffer message = new StringBuffer();
		
		message.append("<com:submitNDExIEPD1_0 xmlns:com=\"urn://com.raytheon.ndex.ai.webservice\">");
		message.append("	<com:data >" + StringEscapeUtils.escapeXml(lexsPayload) +  "</com:data>");
		message.append("	<com:submittingAgencyName>" + submittingAgencyName +  "</com:submittingAgencyName>");
		message.append("	<com:fileName>" + fileName +  "</com:fileName>");
		message.append("</com:submitNDExIEPD1_0>");

		return message.toString();
	}

	
	public String createNdexFormatFileName(@Header(value = "AgencyORI")String ori) throws Exception {

		log.debug("Input ORI: " + ori);
		
		String fileName = null;
		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmm");
		String dateString = formatter.format(calendar.getTime());

		fileName = StringUtils.join(new String[] { ori, dateString,
				uniqueIDGenerator.getNextMessageIDInString() }, '_');

		fileName = fileName + ".xml";
		log.debug("N-DEx File Name: " + fileName);

		return fileName;
	}

	public String createNDexTestFileName(@Header(value="CamelFileName") String camelFileName)
	{
		camelFileName = "TEST" + camelFileName;
		
		return camelFileName;
	}
	
	public String getIncidentIDFromFileName(@Header(value="CamelFileName") String camelFileName)
	{
		
		String agencyOri = StringUtils.substringBetween(camelFileName, "INCIDENT_", "_DATE");

		if (StringUtils.isBlank(agencyOri))
		{
			return "UnknownIncidentID";
		}	
		
		return agencyOri;
	}

	public UniqueIDGenerator getUniqueIDGenerator() {
		return uniqueIDGenerator;
	}

	public void setUniqueIDGenerator(UniqueIDGenerator uniqueIDGenerator) {
		this.uniqueIDGenerator = uniqueIDGenerator;
	}


}
