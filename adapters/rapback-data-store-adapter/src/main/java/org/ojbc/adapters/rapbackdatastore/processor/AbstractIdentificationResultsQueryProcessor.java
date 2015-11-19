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
package org.ojbc.adapters.rapbackdatastore.processor;

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_STRUCTURES_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_XMIME;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_XOP;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Element;

public class AbstractIdentificationResultsQueryProcessor {
	
	protected enum QueryResponseElementName{
		OrganizationIdentificationInitialResultsQueryResults, 
		OrganizationIdentificationSubsequentResultsQueryResults, 
		StateIdentificationSearchResultDocument,
		FBIIdentificationSearchResultDocument,
		StateCriminalHistoryRecordDocument,
		FBIIdentityHistorySummaryDocument,
		Include,
	}
	
	protected enum DocumentId{
		fbiSearchResultDocument, 
		fbiIdentityHistorySummaryDocument,
		stateCriminalHistoryRecordDocument,
		stateSearchResultDocument
	}

	protected static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
	
	protected static final String CID = "cid:";

	protected static final String ATTACHMENT_URL_FORE_STRING = "http://ojbc.org/identification/results/";

	protected final Log log = LogFactory.getLog(this.getClass());

    @Resource
    protected RapbackDAO rapbackDAO;

    protected DocumentBuilder documentBuilder;

    public AbstractIdentificationResultsQueryProcessor() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

	protected void appendDocumentElement(Element parentElement, QueryResponseElementName elementName, String documentId) {
		Element fbiIdentificationSearchResultDocument = 
			XmlUtils.appendElement(parentElement, 
					NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, 
					elementName.name());
		XmlUtils.addAttribute(fbiIdentificationSearchResultDocument, NS_STRUCTURES_30, "id", documentId );
		XmlUtils.addAttribute(fbiIdentificationSearchResultDocument, NS_XMIME, "contentType", CONTENT_TYPE_TEXT_PLAIN);
		Element include = 
			XmlUtils.appendElement(fbiIdentificationSearchResultDocument, NS_XOP, QueryResponseElementName.Include.name());
		String hrefValue = getHrefValue(documentId);
		XmlUtils.addAttribute(include, null, "href", hrefValue);
	}

	protected void addAttachment(Exchange exchange,
			byte[] attachment, String documentId) {
		String attachmentId = ATTACHMENT_URL_FORE_STRING + documentId;  
		exchange.getIn().addAttachment(attachmentId, new DataHandler(new ByteArrayDataSource(attachment, CONTENT_TYPE_TEXT_PLAIN)));
	}

	private String getHrefValue(String documentId) {
		String attachmentId = CID + ATTACHMENT_URL_FORE_STRING + documentId;
		return attachmentId;
	}

}
