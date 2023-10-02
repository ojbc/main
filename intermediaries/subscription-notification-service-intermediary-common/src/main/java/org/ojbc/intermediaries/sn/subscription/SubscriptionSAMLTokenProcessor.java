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

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.ojbc.intermediaries.sn.exception.InvalidSAMLTokenException;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;

public class SubscriptionSAMLTokenProcessor {

	private static final Log log = LogFactory.getLog(SubscriptionSAMLTokenProcessor.class);
	
	public void retrieveSAMLTokenFromMessageAndAddCamelHeader(Exchange exchange) throws Exception
	{
		String samlFederationID = null;
		String samlEmailAddress = null;
		String samlFirstName = null;
		String samlLastName = null;
		String samlEmployerOri = null;
		String samlEmployerAgencyName = null;
		
		try
		{
			Message cxfMessage = exchange.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE, Message.class);
			SAMLTokenPrincipal assertion = SAMLTokenUtils.getSamlTokenFromCxfMessage(cxfMessage);
			
			if (assertion != null)
			{
				samlFederationID = SAMLTokenUtils.getAttributeValueFromSamlToken(assertion, SamlAttribute.FederationId);
				log.debug("Federation ID in SAML Token: " + samlFederationID);

				samlEmailAddress = SAMLTokenUtils.getAttributeValueFromSamlToken(assertion, SamlAttribute.EmailAddressText);
				samlFirstName = SAMLTokenUtils.getAttributeValueFromSamlToken(assertion, SamlAttribute.GivenName);
				samlLastName = SAMLTokenUtils.getAttributeValueFromSamlToken(assertion, SamlAttribute.SurName);
				samlEmployerOri = SAMLTokenUtils.getAttributeValueFromSamlToken(assertion, SamlAttribute.EmployerORI);
				samlEmployerAgencyName = SAMLTokenUtils.getAttributeValueFromSamlToken(assertion, SamlAttribute.EmployerName);
				log.debug("Email Address in SAML Token: " + samlEmailAddress);
				log.debug("First Name in SAML Token: " + samlFirstName);
				log.debug("Last Name in SAML Token: " + samlLastName);
				log.debug("ORI in SAML Token: " + samlEmployerOri);
				log.debug("Agency Name in SAML Token: " + samlEmployerAgencyName);

			}	
			
			if (StringUtils.isNotEmpty(samlFederationID))
			{	
				exchange.getIn().setHeader("saml_FederationID", samlFederationID);
			}

			if (StringUtils.isNotEmpty(samlEmailAddress))
			{	
				exchange.getIn().setHeader("saml_EmailAddress", samlEmailAddress);
			}

			if (StringUtils.isNotEmpty(samlFirstName))
			{	
				exchange.getIn().setHeader("saml_FirstName", samlFirstName);
			}

			if (StringUtils.isNotEmpty(samlLastName))
			{	
				exchange.getIn().setHeader("saml_LastName", samlLastName);
			}

			if (StringUtils.isNotEmpty(samlEmployerOri))
			{	
				exchange.getIn().setHeader("saml_EmployerOri", samlEmployerOri);
			}

			if (StringUtils.isNotEmpty(samlEmployerAgencyName))
			{	
				exchange.getIn().setHeader("saml_EmployerAgencyName", samlEmployerOri);
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
