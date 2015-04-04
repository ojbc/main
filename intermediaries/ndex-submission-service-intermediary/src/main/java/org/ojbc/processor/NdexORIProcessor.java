package org.ojbc.processor;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NdexORIProcessor {

	private static final Log log = LogFactory.getLog(NdexORIProcessor.class);

	private List<String> ndexTestORIList;

	//read in comma separated list of ORIs and write to an array
	public NdexORIProcessor(String ndexTestORIs) {
		
		if (StringUtils.isNotBlank(ndexTestORIs))
		{	
			ndexTestORIList = Arrays.asList(ndexTestORIs.split(","));
		}
		
	}
	
	
	//Determine if data owner agency is authorized to report to N-DEx
	public void confirmNdexTestOri(Exchange exchange, @Header("AgencyORI") String submittingORI){

		boolean result = isOriInList(submittingORI,ndexTestORIList);
		
		if (result)
		{	
			log.debug("ORI IS a test ORI, prepend 'TEST' to filename: "+submittingORI);
		}	
		else
		{
			log.debug("ORI is NOT a test ORI: "+submittingORI);
		}	
		
		exchange.getIn().setHeader("ndexTestORI", result);
			
	}
	
	boolean isOriInList(String submittingORI, List<String> authorizedOris) {
		if (authorizedOris == null)
		{
			return false;
		}	
			
		//determine if submitting ORI exists in the list of authorized ORIs
		for(String s:authorizedOris){
			if(s.replaceAll("\\s","").equalsIgnoreCase(submittingORI)){
				return true;
			}
		}
		
		return false;
	}
	
}
