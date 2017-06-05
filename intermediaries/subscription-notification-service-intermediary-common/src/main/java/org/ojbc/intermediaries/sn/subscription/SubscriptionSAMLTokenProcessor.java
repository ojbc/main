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
package org.ojbc.intermediaries.sn.subscription;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.ojbc.intermediaries.sn.exception.InvalidSAMLTokenException;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.XMLObject;

public class SubscriptionSAMLTokenProcessor {

	private static final Log log = LogFactory.getLog(SubscriptionSAMLTokenProcessor.class);
	
	public void retrieveSAMLTokenFromMessageAndAddCamelHeader(Exchange exchange) throws Exception
	{
		String samlFederationID = null;
		
		try
		{
			Message cxfMessage = exchange.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE, Message.class);
			SAMLTokenPrincipalImpl token = (SAMLTokenPrincipalImpl)cxfMessage.get("wss4j.principal.result");
			Assertion assertion = token.getToken().getSaml2();
			
			if (assertion != null)
			{
				List<AttributeStatement> attributeStatements =assertion.getAttributeStatements();
				
				AttributeStatement attributeStatement = attributeStatements.get(0);
				
				List<Attribute> attributes  = attributeStatement.getAttributes();
				
				for (Attribute attribute : attributes)
				{
					String attributeName = attribute.getName();
					
					if (attributeName.equals("gfipm:2.0:user:FederationId"))
					{
						XMLObject attributeValue = attribute.getAttributeValues().get(0);
						String attributeValueAsString = attributeValue.getDOM().getTextContent();
						log.debug(attributeValueAsString);
						
						samlFederationID = attributeValueAsString;
						break;
					}
					
					
				}	
				
			}	
			
			if (StringUtils.isNotEmpty(samlFederationID))
			{	
				exchange.getIn().setHeader("saml_FederationID", samlFederationID);
			}
		}	
		catch(Exception ex)
		{
			throw new InvalidSAMLTokenException("Unable to invoke service with provided security context");
		}
		
		if (StringUtils.isEmpty(samlFederationID))
		{
			throw new InvalidSAMLTokenException("Unable to invoke service with provided security context.  No federation ID provided in SAML token.");
		}
		
	}
	
}
