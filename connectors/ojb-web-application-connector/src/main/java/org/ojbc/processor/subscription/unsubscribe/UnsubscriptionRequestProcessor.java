package org.ojbc.processor.subscription.unsubscribe;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.processor.MessageProcessor;
import org.ojbc.util.camel.security.saml.OJBSamlMap;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.UnsubscriptionInterface;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.opensaml.xml.signature.SignatureConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UnsubscriptionRequestProcessor implements CamelContextAware, UnsubscriptionInterface {

	/**
	 * Camel context needed to use producer template to send messages
	 */
	protected CamelContext camelContext;
	
	private MessageProcessor unsubscriptionMessageProcessor;
	
	private OJBSamlMap OJBSamlMap;
	
	private static final Log log = LogFactory.getLog( UnsubscriptionRequestProcessor.class );
	
	private boolean allowQueriesWithoutSAMLToken;
	
	@Override
	public void unsubscribe(String subscriptionIdentificationId, String topic, String federatedQueryID,
			Element samlToken) throws Exception {
		
		try
		{
			if (allowQueriesWithoutSAMLToken)
			{	
				if (samlToken == null)
				{
					//Add SAML token to request call
					samlToken = SAMLTokenUtils.createStaticAssertionAsElement("https://idp.ojbc-local.org:9443/idp/shibboleth", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
		
				}	
			}	
			
			if (samlToken == null)
			{
				throw new Exception("No SAML token provided. Unable to perform query.");
			}	
			
			Document requestMessage = RequestMessageBuilderUtilities.createUnubscriptionRequest(subscriptionIdentificationId, topic);
			
			log.info("Unsubscribe Message:");
			XmlUtils.printNode(requestMessage);
			
			//Create exchange
			Exchange senderExchange = new DefaultExchange(camelContext, ExchangePattern.InOnly);
			
			//Set exchange body to XML Request message
			senderExchange.getIn().setBody(requestMessage);
			
			//Set reply to and WS-Addressing message ID
			senderExchange.getIn().setHeader("federatedQueryRequestGUID", federatedQueryID);
			
			//Set the token header so that CXF can retrieve this on the outbound call
			String tokenID = senderExchange.getExchangeId();
			senderExchange.getIn().setHeader("tokenID", tokenID);
	
			OJBSamlMap.putToken(tokenID, samlToken);
	
			unsubscriptionMessageProcessor.sendResponseMessage(camelContext, senderExchange);
				
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw(ex);
		}
		
		
	}

	public CamelContext getCamelContext() {
		return camelContext;
	}

	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;

	}

	public OJBSamlMap getOJBSamlMap() {
		return OJBSamlMap;
	}


	public void setOJBSamlMap(OJBSamlMap oJBSamlMap) {
		OJBSamlMap = oJBSamlMap;
	}

	public boolean isAllowQueriesWithoutSAMLToken() {
		return allowQueriesWithoutSAMLToken;
	}

	public void setAllowQueriesWithoutSAMLToken(boolean allowQueriesWithoutSAMLToken) {
		this.allowQueriesWithoutSAMLToken = allowQueriesWithoutSAMLToken;
	}

	public MessageProcessor getUnsubscriptionMessageProcessor() {
		return unsubscriptionMessageProcessor;
	}

	public void setUnsubscriptionMessageProcessor(
			MessageProcessor unsubscriptionMessageProcessor) {
		this.unsubscriptionMessageProcessor = unsubscriptionMessageProcessor;
	}

	

}
