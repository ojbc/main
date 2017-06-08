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
package org.ojbc.processor.subscription.subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.model.subscription.response.SubscriptionAccessDenialResponse;
import org.ojbc.web.model.subscription.response.SubscriptionInvalidEmailResponse;
import org.ojbc.web.model.subscription.response.SubscriptionInvalidSecurityTokenResponse;
import org.ojbc.web.model.subscription.response.SubscriptionRequestErrorResponse;
import org.ojbc.web.model.subscription.response.SubscriptionSuccessResponse;
import org.ojbc.web.model.subscription.response.UnsubscriptionAccessDenialResponse;
import org.ojbc.web.model.subscription.response.common.SubscriptionFaultResponse;
import org.ojbc.web.model.subscription.response.common.SubscriptionResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SubscriptionResponseProcessor {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(SubscriptionResponseProcessor.class.getName());
				
	
	/**
	 * Success or 'Unsubscribe Access denial' will only return a single object in the list whereas 
	 * a 'fault' type of response can have multiple message objects in the List
	 */
	public List<SubscriptionResponse> processResponse(Document responseDocument) throws Exception{
		
		List<SubscriptionResponse> responseList = new ArrayList<SubscriptionResponse>();
				
		if(isSubscriptionSuccessResponse(responseDocument)){
		
			SubscriptionResponse successResponse = processSubscriptionSuccessResponse(responseDocument);
			
			responseList.add(successResponse);
				
		}else if(isSubscriptionFaultResponse(responseDocument)){
		
			List<SubscriptionFaultResponse> subFaultResponseList = processSubscriptionFaultResponse(responseDocument);
			
			responseList.addAll(subFaultResponseList);
						
		}else if(isUnsubscriptionAccessDenialResponse(responseDocument)){
			
			SubscriptionResponse unsubDenialResponse = processUnsubscriptionAccessDenialResponse(responseDocument);
			
			responseList.add(unsubDenialResponse);
			
		}else{
			
			throw new Exception("Unable to determine type of subscription response");
		}
			
		return responseList;
	}
	
	
	private SubscriptionResponse processSubscriptionSuccessResponse(Document responseDoc) throws Exception{
		
		SubscriptionSuccessResponse successResponse = new SubscriptionSuccessResponse();
		
		Node subscribeResponseNode = XmlUtils.xPathNodeSearch(responseDoc, "b-2:SubscribeResponse");
		
		Node subReferenceNode =  XmlUtils.xPathNodeSearch(subscribeResponseNode, "b-2:SubscriptionReference");
				
		String sAddress = XmlUtils.xPathStringSearch(subReferenceNode, "add:Address");
		
		if(StringUtils.isNotBlank(sAddress)){
			
			successResponse.setSubReferenceAddress(sAddress.trim());			
		}
				
		String sCurrentTime = XmlUtils.xPathStringSearch(subReferenceNode, "b-2:CurrentTime");
		
		if(StringUtils.isNotBlank(sCurrentTime)){
			
			successResponse.setSubReferenceCurrentTime(sCurrentTime.trim());
		}				
				
		String subCreatedIndicator = XmlUtils.xPathStringSearch(subscribeResponseNode, "subresp-exch:SubscriptionResponseMessage/subresp-ext:SubscriptionCreatedIndicator");     
		
		boolean bSubscriptionCreated = Boolean.parseBoolean(subCreatedIndicator);
		
		successResponse.setSubscriptionCreatedIndicator(bSubscriptionCreated);
				
		return successResponse;
	}
	
	private boolean hasAccessDenialNode(Node subFaultRespMsgNode) throws Exception{
		
		Node accessDenialNode = XmlUtils.xPathNodeSearch(subFaultRespMsgNode, "subfltrsp-ext:AccessDenial");
		
		return accessDenialNode != null;		
	}

	private boolean hasInvalidEmailNode(Node subFaultRespMsgNode) throws Exception{
		
		Node invalidEmailListNode = XmlUtils.xPathNodeSearch(subFaultRespMsgNode, "subfltrsp-ext:InvalidEmailAddressList");
		
		return invalidEmailListNode != null;		
	}
	
	private boolean hasRequestErrorNode(Node subFaultRespMsgNode) throws Exception{
		
		Node requestErrorNode = XmlUtils.xPathNodeSearch(subFaultRespMsgNode, "subfltrsp-ext:SubscriptionRequestError");
		
		return requestErrorNode != null;		
	}
	

	private boolean hasInvalidSecTokenNode(Node subFaultRespMsgNode) throws Exception{
		
		Node invalidSecTokenNode = XmlUtils.xPathNodeSearch(subFaultRespMsgNode, "subfltrsp-ext:InvalidSecurityToken");
				
		return invalidSecTokenNode != null;		
	}
		
	private List<SubscriptionFaultResponse> processSubscriptionFaultResponse(
			Document responseDoc) throws Exception {

		List<SubscriptionFaultResponse> rSubFaultResponseList = new ArrayList<SubscriptionFaultResponse>();

		Node rootSubFailedFaultNode = XmlUtils.xPathNodeSearch(responseDoc,
				"b-2:SubscribeCreationFailedFault");

		Node subFaultRespMsgNode = XmlUtils.xPathNodeSearch(rootSubFailedFaultNode,
				"subfltrsp-exch:SubscriptionFaultResponseMessage");
		
		// note all the sub fault type responses have this single same timetamp one level up
		String sTimeStamp = XmlUtils.xPathStringSearch(rootSubFailedFaultNode, "bf2:Timestamp");
				
		
		if(hasAccessDenialNode(subFaultRespMsgNode)){
			
			SubscriptionFaultResponse subFaultResponse = processAccessDenialSubResponse(subFaultRespMsgNode);			
			
			if(subFaultResponse != null){
				
				subFaultResponse.setTimestamp(sTimeStamp);
				
				rSubFaultResponseList.add(subFaultResponse);				
			}
		}
		
		if(hasInvalidEmailNode(subFaultRespMsgNode)){
			
			SubscriptionFaultResponse subFaultResponse = processInvalidEmailSubResponse(subFaultRespMsgNode);
			
			if(subFaultResponse != null){	
				
				subFaultResponse.setTimestamp(sTimeStamp);
				
				rSubFaultResponseList.add(subFaultResponse);				
			}			
		}
		
		if(hasRequestErrorNode(subFaultRespMsgNode)){
			
			SubscriptionFaultResponse subFaultResponse = processRequestErrorSubResponse(subFaultRespMsgNode);
			
			if(subFaultResponse != null){
				
				subFaultResponse.setTimestamp(sTimeStamp);
				
				rSubFaultResponseList.add(subFaultResponse);				
			}
		}

		if(hasInvalidSecTokenNode(subFaultRespMsgNode)){
			
			SubscriptionFaultResponse subFaultResponse = processInvalidSecTokenSubResponse(subFaultRespMsgNode);
			
			if(subFaultResponse != null){
				
				subFaultResponse.setTimestamp(sTimeStamp);
				
				rSubFaultResponseList.add(subFaultResponse);				
			}
		}
		
		return rSubFaultResponseList;
	}

	
	private SubscriptionFaultResponse processInvalidSecTokenSubResponse(Node subFaultRespMsgNode) throws Exception{
		
		SubscriptionInvalidSecurityTokenResponse subFaultResponse = new SubscriptionInvalidSecurityTokenResponse();
		
		Node invalidSecTknNode = XmlUtils.xPathNodeSearch(subFaultRespMsgNode, "subfltrsp-ext:InvalidSecurityToken");
		
		String sInvalidSecTokenIndicator = XmlUtils.xPathStringSearch(invalidSecTknNode, "subfltrsp-ext:InvalidSecurityTokenIndicator");
		
		if(StringUtils.isNotBlank(sInvalidSecTokenIndicator)){
			
			Boolean bInvalidSecIndicator = Boolean.parseBoolean(sInvalidSecTokenIndicator);
			subFaultResponse.setInvalidSecurityTokenIndicator(bInvalidSecIndicator);
		}		
		
		String sInvalidSecTknDescTxt = XmlUtils.xPathStringSearch(invalidSecTknNode, "subfltrsp-ext:InvalidSecurityTokenDescriptionText");
		
		if(StringUtils.isNotBlank(sInvalidSecTknDescTxt)){
			subFaultResponse.setInvalidSecurityTokenDescription(sInvalidSecTknDescTxt.trim());
		}
						
		return subFaultResponse;
	}
	
	
	private SubscriptionFaultResponse processRequestErrorSubResponse(Node subFaultRespMsgNode) throws Exception{
		
		SubscriptionRequestErrorResponse subErrorRequestResponse = new SubscriptionRequestErrorResponse();
		
		Node subReqErrNode = XmlUtils.xPathNodeSearch(subFaultRespMsgNode, "subfltrsp-ext:SubscriptionRequestError");
		
		String sErrorTxt = XmlUtils.xPathStringSearch(subReqErrNode, "subfltrsp-ext:ErrorText");
		
		if(StringUtils.isNotBlank(sErrorTxt)){
			subErrorRequestResponse.setRequestErrorTxt(sErrorTxt.trim());
		}
				
		String systemName = XmlUtils.xPathStringSearch(subReqErrNode, "intel:SystemName");
		
		if(StringUtils.isNotBlank(systemName)){
			subErrorRequestResponse.setRequestErrorSystemName(systemName.trim());
		}
						
		return subErrorRequestResponse;	
	}
		
	
	
	private SubscriptionFaultResponse processInvalidEmailSubResponse(Node subFaultRespMsgNode) throws Exception{
		
		SubscriptionInvalidEmailResponse subInvalidEmailResp = new SubscriptionInvalidEmailResponse();
		
		NodeList invalidEmailNodeList = XmlUtils.xPathNodeListSearch(subFaultRespMsgNode, "subfltrsp-ext:InvalidEmailAddressList/nc:EmailRecipientAddressID");
		
		List<String> sInvalidEmailList = new ArrayList<String>();
				
		for(int i=0; i < invalidEmailNodeList.getLength(); i++){
			
			Node iInvalidEmailNode = invalidEmailNodeList.item(i);
			
			String sInvalidEmail = iInvalidEmailNode.getTextContent();
			
			if(StringUtils.isNotBlank(sInvalidEmail)){
				
				sInvalidEmailList.add(sInvalidEmail.trim());
			}
		}
		
		if(sInvalidEmailList != null && !sInvalidEmailList.isEmpty()){
			
			subInvalidEmailResp.setInvalidEmailList(sInvalidEmailList);
		}
				
		return subInvalidEmailResp;
	}
	

	private SubscriptionFaultResponse processAccessDenialSubResponse(Node subFaultNode) throws Exception{
		
		SubscriptionAccessDenialResponse subscriptionAccessDenialResponse = new SubscriptionAccessDenialResponse();
		
		Node accessDenialNode = XmlUtils.xPathNodeSearch(subFaultNode, "subfltrsp-ext:AccessDenial");
		
		String accessDenialIndicator = XmlUtils.xPathStringSearch(accessDenialNode, "subfltrsp-ext:AccessDenialIndicator");
		
		if(StringUtils.isNotBlank(accessDenialIndicator)){
			
			boolean bAccessDenied = Boolean.parseBoolean(accessDenialIndicator);
			
			subscriptionAccessDenialResponse.setAccessDenialIndicator(bAccessDenied);			
		}		
		
		String accessDenialSysName = XmlUtils.xPathStringSearch(accessDenialNode, "subfltrsp-ext:AccessDenyingSystemNameText");
		
		if(StringUtils.isNotBlank(accessDenialSysName)){
			
			subscriptionAccessDenialResponse.setAccessDenyingSystemName(accessDenialSysName.trim());
		}
				
		String accessDenialReason = XmlUtils.xPathStringSearch(accessDenialNode, "subfltrsp-ext:AccessDenialReasonText");
		
		if(StringUtils.isNotBlank(accessDenialReason)){
			
			subscriptionAccessDenialResponse.setAccessDenyingReason(accessDenialReason.trim());
		}
						
		return subscriptionAccessDenialResponse;
	}
	
	
	
	private SubscriptionResponse processUnsubscriptionAccessDenialResponse(Document doc) throws Exception{
		
		UnsubscriptionAccessDenialResponse unsubAccessDenyResponse = new UnsubscriptionAccessDenialResponse();
		
		Node rootUnsubFaultNode = XmlUtils.xPathNodeSearch(doc, "b-2:UnableToDestroySubscriptionFault");
		
		Node accessDenyNode = XmlUtils.xPathNodeSearch(rootUnsubFaultNode, "subfltrsp-exch:SubscriptionFaultResponseMessage/subfltrsp-ext:AccessDenial");
								
		String accessDenialIndicator = XmlUtils.xPathStringSearch(accessDenyNode, "subfltrsp-ext:AccessDenialIndicator");
		
		if(StringUtils.isNotBlank(accessDenialIndicator)){
			
			boolean bAccessDenied = Boolean.parseBoolean(accessDenialIndicator);
						
			unsubAccessDenyResponse.setAccessDenialIndicator(bAccessDenied);
		}
				
		String accessDenialSysName = XmlUtils.xPathStringSearch(accessDenyNode, "subfltrsp-ext:AccessDenyingSystemNameText");
		
		if(StringUtils.isNotBlank(accessDenialSysName)){
						
			unsubAccessDenyResponse.setAccessDenyingSystemName(accessDenialSysName.trim());
		}		
		
		String accessDenialReason = XmlUtils.xPathStringSearch(accessDenyNode, "subfltrsp-ext:AccessDenialReasonText");
		
		if(StringUtils.isNotBlank(accessDenialReason)){
						
			unsubAccessDenyResponse.setAccessDenyingReason(accessDenialReason.trim());
		}		
				
		String sTimeStamp = XmlUtils.xPathStringSearch(rootUnsubFaultNode, "bf2:Timestamp");
		
		if(StringUtils.isNotBlank(sTimeStamp)){
			
			unsubAccessDenyResponse.setTimestamp(sTimeStamp);
		}
		
		return unsubAccessDenyResponse;
	}


	public static boolean isSubscriptionSuccessResponse(Document responseDoc) throws Exception{
					
		Node subRefNode = XmlUtils.xPathNodeSearch(responseDoc, "//b-2:SubscribeResponse/b-2:SubscriptionReference");
		
		boolean hasSubRefNode = subRefNode != null; 
		
		return hasSubRefNode;		
	}
	
	
	public static boolean isSubscriptionFaultResponse(Document responseDoc) throws Exception{
		
		Node subFaultNode = XmlUtils.xPathNodeSearch(responseDoc, "//b-2:SubscribeCreationFailedFault/subfltrsp-exch:SubscriptionFaultResponseMessage");

		boolean hasSubFaultNode = subFaultNode != null;
		
		return hasSubFaultNode;		
	}
	
	public static boolean isUnsubscriptionAccessDenialResponse(Document responseDoc) throws Exception{
		
		Node unsubAcesDenyNode = XmlUtils.xPathNodeSearch(responseDoc, 
				"//b-2:UnableToDestroySubscriptionFault/subfltrsp-exch:SubscriptionFaultResponseMessage/subfltrsp-ext:AccessDenial");
		
		boolean hasUnsubAccessDenyNode = unsubAcesDenyNode != null;
		
		return hasUnsubAccessDenyNode;
	}
		
}

