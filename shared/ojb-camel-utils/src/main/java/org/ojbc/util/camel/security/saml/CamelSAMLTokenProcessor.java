package org.ojbc.util.camel.security.saml;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.ws.security.SAMLTokenPrincipal;
import org.w3c.dom.Element;

public class CamelSAMLTokenProcessor {

	private OJBSamlMap OJBSamlMap;
	
	private static final Log log = LogFactory.getLog(CamelSAMLTokenProcessor.class);
	
	public void retrieveSAMLTokenFromMessage(Exchange exchange)
	{
		Message cxfMessage = exchange.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE, Message.class);
		SAMLTokenPrincipal token = (SAMLTokenPrincipal)cxfMessage.get("wss4j.principal.result");
		Element assertionElement = null;
		
		if (token != null)
		{	
			assertionElement = token.getToken().getSaml2().getDOM();
		}
			
		if (assertionElement != null)
		{
			//Set the token header so that CXF can retrieve this on the outbound call
			String tokenID = exchange.getExchangeId();
			exchange.getIn().setHeader("tokenID", tokenID);

			log.info("Retrieved SAML token from inbound message and adding to token map with ID: " + tokenID);
			
			OJBSamlMap.putToken(tokenID, assertionElement);
		}	
		
	}

	public OJBSamlMap getOJBSamlMap() {
		return OJBSamlMap;
	}

	public void setOJBSamlMap(OJBSamlMap oJBSamlMap) {
		OJBSamlMap = oJBSamlMap;
	}
	
}
