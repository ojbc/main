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
package org.ojbc.bundles.intermediaries.parole.event;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.ojbc.bundles.intermediaries.parole.event.utils.NIEMXMLUtils;
import org.ojbc.bundles.intermediaries.parole.event.utils.PersonSearchRequest;
import org.ojbc.bundles.intermediaries.parole.event.utils.PersonSearchRequestDomUtils;
import org.ojbc.bundles.intermediaries.parole.event.utils.SearchFieldMetadata;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PersonSearchRequestFactory {
	
	private Logger logger = Logger.getLogger(PersonSearchRequestFactory.class);
	
	
	public Document createPersonSearchRequest(PersonSearchRequest psr) throws Exception{
		
		Document doc = OJBCXMLUtils.createDocument();
		
		Element personSearchRequestElement = NIEMXMLUtils.createPersonsSearchRequestElement(doc, "SM003");

		Element personElement = PersonSearchRequestDomUtils.createPersonsElement(doc, psr);
		
		personSearchRequestElement.appendChild(personElement);
		doc.appendChild(personSearchRequestElement);
		
		if (psr.getSourceSystems()==null || psr.getSourceSystems().size()==0)
		{
			throw new IllegalStateException("No source systems specified");
		}	
		
		for (String sourceSystemName : psr.getSourceSystems())
		{
			Element sourceSystemNameElement = NIEMXMLUtils.createSourceSystemElement(doc, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, sourceSystemName);
			personSearchRequestElement.appendChild(sourceSystemNameElement);
		}	
		
		if (psr.getPersonSurNameMetaData() == SearchFieldMetadata.ExactMatch || psr.getPersonGivenNameMetaData() == SearchFieldMetadata.ExactMatch )
		{
			Element exactMatchMetaData = NIEMXMLUtils.createSearchMetaData(doc, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, SearchFieldMetadata.ExactMatch);
			personSearchRequestElement.appendChild(exactMatchMetaData);
		}	

		if (psr.getPersonSurNameMetaData() == SearchFieldMetadata.StartsWith || psr.getPersonGivenNameMetaData() == SearchFieldMetadata.StartsWith )
		{
			Element startsWithMetaData = NIEMXMLUtils.createSearchMetaData(doc, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, SearchFieldMetadata.StartsWith);
			personSearchRequestElement.appendChild(startsWithMetaData);
		}	
		
		if (StringUtils.isNotBlank(psr.getOnBehalfOf()) || StringUtils.isNotBlank(psr.getPurpose()))
		{
			Element auditMetaData = NIEMXMLUtils.createSearchMetaDataPurposeOnBehalfOf(doc, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, "OBO3", psr.getOnBehalfOf(), psr.getPurpose());
			personSearchRequestElement.appendChild(auditMetaData);
		}	

//		String personSearchReqXml = OJBUtils.getStringFromDocument(doc);
		
		logger.debug("\n\n Person Search Request Document: \n" + OJBUtils.getStringFromDocument(doc) + "\n\n");
		
		return doc;				
	}
	
}
