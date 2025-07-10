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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.ojbc.intermediaries.sn.exception.InvalidSAMLTokenException;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;

public class SubscriptionSAMLTokenProcessor {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(SubscriptionSAMLTokenProcessor.class);
	
	public void retrieveSAMLTokenFromMessageAndAddCamelHeader(Exchange exchange) throws Exception
	{
		String samlFederationID = null;
		try
		{
			Message cxfMessage = exchange.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE, Message.class);
			SAMLTokenPrincipal assertion = SAMLTokenUtils.getSamlTokenFromCxfMessage(cxfMessage);

			if (assertion != null)
			{
				samlFederationID = SAMLTokenUtils.getSamlAttributeFromCxfMessage(cxfMessage, SamlAttribute.FederationId);

				SAMLTokenUtils.processSamlAttribue(exchange, cxfMessage, SamlAttribute.FederationId, "saml_FederationID");
				SAMLTokenUtils.processSamlAttribue(exchange, cxfMessage, SamlAttribute.EmailAddressText, "saml_EmailAddress");
				SAMLTokenUtils.processSamlAttribue(exchange, cxfMessage, SamlAttribute.GivenName, "saml_FirstName");
				SAMLTokenUtils.processSamlAttribue(exchange, cxfMessage, SamlAttribute.SurName, "saml_LastName");
				SAMLTokenUtils.processSamlAttribue(exchange, cxfMessage, SamlAttribute.EmployerORI, "saml_EmployerOri");
				SAMLTokenUtils.processSamlAttribue(exchange, cxfMessage, SamlAttribute.EmployerName, "saml_EmployerAgencyName");
			}	
		}	
		catch(Exception ex)
		{
			throw new InvalidSAMLTokenException("Unable to invoke service with provided security context");
		}
		if (StringUtils.isBlank(samlFederationID))
		{
			throw new InvalidSAMLTokenException("Unable to invoke service with provided security context.  "
					+ "No federation ID provided in SAML token.");
		}
		
	}
	
}
