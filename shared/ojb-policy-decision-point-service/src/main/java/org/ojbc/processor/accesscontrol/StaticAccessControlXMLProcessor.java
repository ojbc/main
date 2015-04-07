package org.ojbc.processor.accesscontrol;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.ExchangeException;
import org.apache.camel.Header;
import org.ojbc.util.camel.helper.AccessControlUtils;
import org.w3c.dom.Document;

/**
 * This simple wrapper class will return access allowed, denied and error responses.
 * This is useful as wait to integrate with access control web services from 3rd parties
 * who are not yet ready to integrate.
 * 
 * @author yogeshchawla
 *
 */
public class StaticAccessControlXMLProcessor {

	public Document returnStaticAccessDeniedResponse() throws ParserConfigurationException
	{
		return AccessControlUtils.buildAccessControlResponse(true, "OJB PDP Service");
		
	}
	
	public Document returnStaticAccessAllowedResponse() throws ParserConfigurationException
	{
		return AccessControlUtils.buildAccessControlResponse(false, "OJB PDP Service.");
	}	

	public Document buildAccessControlErrorResponse(@ExchangeException Exception exception, 
			@Header("AccessControlRequestingSystemName") String accessControlRequestingSystemName) throws ParserConfigurationException
	{
		//TODO: Make system name dynamic
		return AccessControlUtils.buildAccessControlErrorResponse(exception, "OJB PDP Service");
	}
}
