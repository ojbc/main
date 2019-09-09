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
package org.ojbc.audit.enhanced.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;

public abstract class AbstractUserInfoProcessor {

	private static final Log log = LogFactory.getLog(AbstractUserInfoProcessor.class);
	
	public abstract Integer auditUserInfo(Exchange exchange);
	
	UserInfo processUserInfoRequest(Assertion assertion) throws Exception
	{
		UserInfo userInfoRequest = new UserInfo();

		
		if (assertion != null)
		{
			List<AttributeStatement> attributeStatements =assertion.getAttributeStatements();
			
			AttributeStatement attributeStatement = attributeStatements.get(0);
			
			List<Attribute> attributes  = attributeStatement.getAttributes();
			
			for (Attribute attribute : attributes)
			{
				String attributeName = attribute.getName();
				
				if (attributeName.equals("gfipm:2.0:user:GivenName"))
				{
					XMLObject attributeValue = attribute.getAttributeValues().get(0);
					String attributeValueAsString = attributeValue.getDOM().getTextContent();
					log.debug(attributeValueAsString);
					
					userInfoRequest.setUserFirstName(attributeValueAsString);
				}
				
				if (attributeName.equals("gfipm:2.0:user:SurName"))
				{
					XMLObject attributeValue = attribute.getAttributeValues().get(0);
					String attributeValueAsString = attributeValue.getDOM().getTextContent();
					log.debug(attributeValueAsString);
					
					userInfoRequest.setUserLastName(attributeValueAsString);
				}
				
				if (attributeName.equals("gfipm:2.0:user:EmployerName"))
				{
					XMLObject attributeValue = attribute.getAttributeValues().get(0);
					String attributeValueAsString = attributeValue.getDOM().getTextContent();
					log.debug(attributeValueAsString);
					
					userInfoRequest.setEmployerName(attributeValueAsString);
				}
				
				if (attributeName.equals("gfipm:2.0:user:EmployerSubUnitName"))
				{
					XMLObject attributeValue = attribute.getAttributeValues().get(0);
					String attributeValueAsString = attributeValue.getDOM().getTextContent();
					log.debug(attributeValueAsString);

					userInfoRequest.setEmployerSubunitName(attributeValueAsString);
				}
				
				if (attributeName.equals("gfipm:2.0:user:EmailAddressText"))
				{
					XMLObject attributeValue = attribute.getAttributeValues().get(0);
					String attributeValueAsString = attributeValue.getDOM().getTextContent();
					log.debug(attributeValueAsString);

					userInfoRequest.setUserEmailAddress(attributeValueAsString);
									     					
				}	
				
				if (attributeName.equals("gfipm:2.0:user:IdentityProviderId"))
				{
					XMLObject attributeValue = attribute.getAttributeValues().get(0);
					String attributeValueAsString = attributeValue.getDOM().getTextContent();
					log.debug(attributeValueAsString);

					userInfoRequest.setIdentityProviderId(attributeValueAsString);
									     					
				}	

				if (attributeName.equals("gfipm:2.0:user:FederationId"))
				{
					XMLObject attributeValue = attribute.getAttributeValues().get(0);
					String attributeValueAsString = attributeValue.getDOM().getTextContent();
					log.debug(attributeValueAsString);

					userInfoRequest.setFederationId(attributeValueAsString);
									     					
				}	

				if (attributeName.equals("gfipm:2.0:user:EmployerORI"))
				{
					XMLObject attributeValue = attribute.getAttributeValues().get(0);
					String attributeValueAsString = attributeValue.getDOM().getTextContent();
					log.debug(attributeValueAsString);

					userInfoRequest.setEmployerOri(attributeValueAsString);
									     					
				}	

				
			}
				
		}	
		
        return userInfoRequest;
	}
	
}
