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
package org.ojbc.bundles.intermediaries.probation.event;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class PersonSearchResponseProcessor {
	
	private Logger logger = Logger.getLogger(PersonSearchResponseProcessor.class);
	
	public void setFbiIdHeaderFromPersonSearchResponse(Exchange exchange, Document personSearchResponse) throws Exception{
		
		NodeList responseNodeList = XmlUtils.xPathNodeListSearch(personSearchResponse, "//psres:PersonSearchResult");
		
		if(responseNodeList != null && responseNodeList.getLength() == 1){
			
			String fbiId = XmlUtils.xPathStringSearch(personSearchResponse, 
					"//psres:PersonSearchResult/emrm-ext:Person/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
			
			logger.info("\n\n\n Processor returning fbiId:  " + fbiId + "\n\n\n");
			
			exchange.getIn().setHeader("fbiId", fbiId);				
		
		}else{			
			logger.error("\n\n\n WARNING Person Responses NOT = 1, (NOT setting fbi id now)  \n\n\n");
		}	
	}	

}
