package org.ojbc.processor.subscription.validation;

import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.model.subscription.validation.SubscriptionValidationResponse;
import org.w3c.dom.Document;

public class SubscriptionValidationResponseProcessor {
						
	public SubscriptionValidationResponse processResponse(Document validResponseDoc) throws Exception{
		
		SubscriptionValidationResponse subValidResponse = new SubscriptionValidationResponse();
						
		String sSubValidatedIndicator = XmlUtils.xPathStringSearch(validResponseDoc, 
				"/b-2:ValidateResponse/svm:SubscriptionValidationResponseMessage/subresp-ext:SubscriptionValidatedIndicator");
		
		boolean bSubValidatedIndicator = Boolean.parseBoolean(sSubValidatedIndicator);	
		
		subValidResponse.setSubscriptionValidated(bSubValidatedIndicator);
			
		return subValidResponse;
	}
	
}
 