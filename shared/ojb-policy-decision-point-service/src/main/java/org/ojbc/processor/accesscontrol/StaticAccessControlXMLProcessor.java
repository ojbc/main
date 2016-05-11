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

	public Document returnStaticAccessDeniedResponse(@Header("requestedResourceURI") String requestedResourceURI) throws ParserConfigurationException
	{
		return AccessControlUtils.buildAccessControlResponse(true, requestedResourceURI);
		
	}
	
	public Document returnStaticAccessAllowedResponse(@Header("requestedResourceURI") String requestedResourceURI) throws ParserConfigurationException
	{
		return AccessControlUtils.buildAccessControlResponse(false, requestedResourceURI);
	}	

	public Document buildAccessControlErrorResponse(@Header("requestedResourceURI") String requestedResourceURI, @ExchangeException Exception exception, 
			@Header("AccessControlRequestingSystemName") String accessControlRequestingSystemName) throws ParserConfigurationException
	{
		//TODO: Make system name dynamic
		return AccessControlUtils.buildAccessControlErrorResponse(exception, "OJB PDP Service");
	}
}
