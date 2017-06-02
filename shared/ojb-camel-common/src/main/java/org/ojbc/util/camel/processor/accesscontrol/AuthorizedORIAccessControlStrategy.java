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
package org.ojbc.util.camel.processor.accesscontrol;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.ojbc.util.model.accesscontrol.AccessControlResponse;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.XMLObject;

/**
 * In this strategy, we compare the ORI in the SAML token against a list of authorized ORIs.
 * We implement the AccessControlStrategy.
 * 
 * @author yogeshchawla
 *
 */
public class AuthorizedORIAccessControlStrategy implements AccessControlStrategy{


	private static final Log log = LogFactory.getLog(AuthorizedORIAccessControlStrategy.class);
	
	List<String> authorizedORIs;
	
	public AuthorizedORIAccessControlStrategy(String authorizedORIsProperty) {
		
		if (StringUtils.isNotBlank(authorizedORIsProperty))
		{	
			authorizedORIs = Arrays.asList(authorizedORIsProperty.split(","));
		}
		else
		{
			throw new IllegalStateException("Unable to initialize Access Control Strategy.  No ORI's specified in configuration");
		}	

	}

	
	@Override
	public AccessControlResponse authorize(Exchange ex) throws Exception {

		AccessControlResponse accessControlResponse = new AccessControlResponse();
		String employerORI = "";
		
		if (authorizedORIs == null)
		{
			accessControlResponse.setAuthorized(false);
			accessControlResponse.setAccessControlResponseMessage("There are no authorized ORIs to query data.");
			
			return accessControlResponse;
		}	
		
		try
		{
			Message cxfMessage = ex.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE, Message.class);
			SAMLTokenPrincipal token = (SAMLTokenPrincipal)cxfMessage.get("wss4j.principal.result");
			Assertion assertion = token.getToken().getSaml2();
	
			if (assertion != null)
			{
				List<AttributeStatement> attributeStatements =assertion.getAttributeStatements();
				
				AttributeStatement attributeStatement = attributeStatements.get(0);
				
				List<Attribute> attributes  = attributeStatement.getAttributes();
				
				for (Attribute attribute : attributes)
				{
					String attributeName = attribute.getName();
					
					if (attributeName.equals("gfipm:2.0:user:EmployerORI"))
					{
						XMLObject attributeValue = attribute.getAttributeValues().get(0);
						String attributeValueAsString = attributeValue.getDOM().getTextContent();
						log.debug(attributeValueAsString);
	
						employerORI = attributeValueAsString;
						
						break;
					}
					
				}	
			}
	
			if (authorizedORIs.contains(employerORI))
			{
				accessControlResponse.setAuthorized(true);
				accessControlResponse.setAccessControlResponseMessage("Users in the ORI: " + employerORI + " are authorized to run this query.");
	
			}	
			else
			{
				accessControlResponse.setAuthorized(false);
				accessControlResponse.setAccessControlResponseMessage("Users in the ORI: " + employerORI + " are NOT authorized to run this query.");
				
			}	
		} catch (Exception exception)
		{
			accessControlResponse.setAuthorized(false);
			accessControlResponse.setAccessControlResponseMessage(exception.getMessage());
		}
		
			
		return accessControlResponse;
		
	}


}
