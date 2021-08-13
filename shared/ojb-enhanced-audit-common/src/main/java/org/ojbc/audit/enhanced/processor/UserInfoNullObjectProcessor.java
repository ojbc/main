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
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.opensaml.saml.saml2.core.Assertion;

public class UserInfoNullObjectProcessor extends AbstractUserInfoProcessor {

	private static final Log log = LogFactory.getLog(UserInfoNullObjectProcessor.class);
	
	public Integer auditUserInfo(Exchange exchange)
	{
		Integer userInfoPk = null;
		
		try {
			Message cxfMessage = exchange.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE, Message.class);
			SAMLTokenPrincipal token = (SAMLTokenPrincipal)cxfMessage.get("wss4j.principal.result");
			Assertion assertion = token.getToken().getSaml2();

			UserInfo userInfo = processUserInfoRequest(assertion);
			
			log.info("User Info: " + userInfo.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to audit user info request: " + ExceptionUtils.getStackTrace(e));
		}
		
		return userInfoPk;
	}

	
}
