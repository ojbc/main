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
package org.ojbc.intermediaries.sn.dao;

import java.util.List;

import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SendToSubscriptionAdapterORIStrategy implements SendToSubscriptionAdapterStrategy {

	public List<String> authorizedOrisForSubscriptionAdapter;
	
	private static final Log log = LogFactory.getLog(SendToSubscriptionAdapterORIStrategy.class);
	
	@Override
	public boolean sendToAdapter(@Header("subscriptionOwnerOri") String ori) {

		log.info("Authorized ORIs: " + authorizedOrisForSubscriptionAdapter.toString() + ", ORI in message: ##" + ori + "##" + "List size: " + authorizedOrisForSubscriptionAdapter.size());
		
		if (authorizedOrisForSubscriptionAdapter == null || StringUtils.isBlank(ori))
		{
			log.info("either ORI are null or ori is blank, return false");
			
			return false;
		}	
		
		if (authorizedOrisForSubscriptionAdapter.contains(ori.trim()))
		{
			log.info("authorized ori in list, return true");
			
			return true;
		}
		else
		{
			log.info("authorized ori not in list, return false");
			
			return false;
		}	
	}

	public List<String> getAuthorizedOrisForSubscriptionAdapter() {
		return authorizedOrisForSubscriptionAdapter;
	}

	public void setAuthorizedOrisForSubscriptionAdapter(List<String> authorizedOrisForSubscriptionAdapter) {
		this.authorizedOrisForSubscriptionAdapter = authorizedOrisForSubscriptionAdapter;
	}

}
