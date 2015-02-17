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
