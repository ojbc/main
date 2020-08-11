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
package org.ojbc.adapters.rapbackdatastore.processor;

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_STRUCTURES_30;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Element;

public class AbstractIdentificationResultsQueryProcessor extends AbstractSearchQueryProcessor {
	
	protected enum QueryResponseElementName{
		OrganizationIdentificationInitialResultsQueryResults, 
		OrganizationIdentificationSubsequentResultsQueryResults, 
		StateIdentificationSearchResultDocument,
		FBIIdentificationSearchResultDocument,
		StateCriminalHistoryRecordDocument,
		FBIIdentityHistorySummaryDocument,
		NationalSexOffenderRegistryDemographicsDocument,
		NationalSexOffenderRegistrySearchResultDocument,
		DocumentBinary,
		Base64BinaryObject,
		OrganizationIdentificationNsorQueryResults,
		NsorFiveYearCheckDocument
	}
	
	protected enum DocumentId{
		fbiSearchResultDocument, 
		fbiIdentityHistorySummaryDocument,
		stateCriminalHistoryRecordDocument,
		stateSearchResultDocument,
		nationalSexOffenderRegistryDemographicsDocument,
		nationalSexOffenderRegistrySearchResultDocument
	}

	protected final Log log = LogFactory.getLog(this.getClass());

    public AbstractIdentificationResultsQueryProcessor() throws ParserConfigurationException {
    	super();
    }

	protected void appendDocumentElement(Element parentElement, QueryResponseElementName elementName, String documentId, byte[] binaryData) {
		Element element = 
			XmlUtils.appendElement(parentElement, 
					NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, 
					elementName.name());
		XmlUtils.addAttribute(element, NS_STRUCTURES_30, "id", documentId );
		Element documentBinary = 
			XmlUtils.appendElement(element, NS_NC_30, QueryResponseElementName.DocumentBinary.name());
		Element base64BinaryObject = 
				XmlUtils.appendElement(documentBinary, 
						NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, 
						QueryResponseElementName.Base64BinaryObject.name());
		
		base64BinaryObject.setTextContent(Base64.encodeBase64String(binaryData));
	}

}
