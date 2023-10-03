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

import org.apache.camel.Exchange;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml.saml2.core.Assertion;
import org.w3c.dom.Element;

public abstract class AbstractUserInfoProcessor {

	public abstract Integer auditUserInfo(Exchange exchange);
	
	UserInfo processUserInfoRequest(Assertion assertion) throws Exception
	{
		UserInfo userInfoRequest = new UserInfo();

		if (assertion != null)
		{
			Element assertionElement = assertion.getDOM(); 

			userInfoRequest.setUserFirstName(SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.GivenName));
			userInfoRequest.setUserLastName(SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.SurName));
			userInfoRequest.setEmployerName(SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.EmployerName));
			userInfoRequest.setEmployerSubunitName(SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.EmployerSubUnitName));
			userInfoRequest.setUserEmailAddress(SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.EmailAddressText));
			userInfoRequest.setIdentityProviderId(SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.IdentityProviderId));
			userInfoRequest.setFederationId(SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.FederationId));
			userInfoRequest.setEmployerOri(SAMLTokenUtils.getAttributeValue(assertionElement, SamlAttribute.EmployerORI));
		}	
		
        return userInfoRequest;
	}
	
}
