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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.connectors.warrantmod;

import org.apache.camel.Body;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.connectors.warrantmod.dao.WarrantsRepositoryBaseDAO;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

@Component
public class WarrantModificationResponseProcessor {

	private static final Log log = LogFactory.getLog(WarrantModificationResponseProcessor.class);
	
	@Autowired
	private WarrantsRepositoryBaseDAO warrantsRepositoryBaseDAO;
	
	public void process(@Body Document document) throws Exception{
		log.info("Processing warrant modification response ");
		
		String stateWarrantRepositoryId = 
				XmlUtils.xPathStringSearch(document, "/wm-resp-doc:WarrantModificationResponse/jxdm51:Warrant/wm-resp-ext:WarrantAugmentation/wm-resp-ext:StateWarrantRepositoryIdentification/nc30:IdentificationID");
		
		int updatedRowCount = warrantsRepositoryBaseDAO.updateWarrantResponseReceivedIndicator(stateWarrantRepositoryId);
		
		if (updatedRowCount > 0){
			log.info("Successfully processed warrant modification response with state warrant repo id: " + stateWarrantRepositoryId);
		}
		else{
			log.warn("Did not find a qualified row to update for state warrant repo: " + stateWarrantRepositoryId);
		}
	}

}
