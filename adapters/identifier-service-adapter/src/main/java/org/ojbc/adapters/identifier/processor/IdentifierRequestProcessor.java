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
package org.ojbc.adapters.identifier.processor;

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_IDENTIFIER_RESPONSE_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_IDENTIFIER_RESPONSE_DOC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_NC_30;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.lucene.personid.IdentifierGenerationStrategy;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class will handle archive processing.  It will interact with the DAO and then create either a valid response or error response 
 */

@Service
public class IdentifierRequestProcessor {
    private final Log log = LogFactory.getLog(this.getClass());

	private DocumentBuilder documentBuilder;
	private IdentifierGenerationStrategy identifierGenerationStrategy;

	public IdentifierRequestProcessor() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

	public Document returnIdentifierResponse(@Header("personNode") Node personNode) throws Exception
	{
        Document document = documentBuilder.newDocument();
        Element rootElement = appendRootElement(document);
        
        Element identification = XmlUtils.appendElement(rootElement, NS_NC_30, "Identification");
        Element identificationId = XmlUtils.appendElement(identification, NS_NC_30, "IdentificationID");
        
        String personIdentifier = getPersonIdentifier(personNode);
        identificationId.setTextContent(personIdentifier);
        
		return document;
	}

	private String getPersonIdentifier(Node personNode) throws Exception {
		Map<String,Object> personAttributeMap = retrievePersonAttributeMap(personNode);
		String personIdentifierKey = getIdentifierGenerationStrategy().generateIdentifier(personAttributeMap);
		return personIdentifierKey;
	}

	private Element appendRootElement(Document document) {
		Element rootElement = document.createElementNS(
                NS_IDENTIFIER_RESPONSE_DOC,
                NS_PREFIX_IDENTIFIER_RESPONSE_DOC +":IdentifierResponse");
        
        rootElement.setAttribute("xmlns:" + NS_PREFIX_IDENTIFIER_RESPONSE_DOC, NS_IDENTIFIER_RESPONSE_DOC );
        rootElement.setAttribute("xmlns:" + NS_PREFIX_NC_30, NS_NC_30 );
        document.appendChild(rootElement);
        
        return rootElement;
	}
	
	public Map<String, Object> retrievePersonAttributeMap(Node personNode) throws Exception{

		Map<String, Object> personAttributes = new HashMap<String, Object>();
		
		String personFirstName=XmlUtils.xPathStringSearch(personNode, "nc30:PersonName/nc30:PersonGivenName");
		if (StringUtils.isNotBlank(personFirstName)){
			personAttributes.put(IdentifierGenerationStrategy.FIRST_NAME_FIELD, personFirstName);
		}	
		
		String personMiddleName=XmlUtils.xPathStringSearch(personNode, "nc30:PersonName/nc30:PersonMiddleName");
		if (StringUtils.isNotBlank(personMiddleName)){
			personAttributes.put(IdentifierGenerationStrategy.MIDDLE_NAME_FIELD, personMiddleName);
		}	
				
		String personLastName=XmlUtils.xPathStringSearch(personNode, "nc30:PersonName/" + "nc30:PersonSurName");
		if (StringUtils.isNotBlank(personLastName)){
			personAttributes.put(IdentifierGenerationStrategy.LAST_NAME_FIELD, personLastName);
		}	
								
		String personDateOfBirth=XmlUtils.xPathStringSearch(personNode,"nc30:PersonBirthDate/nc30:Date");
		if (StringUtils.isNotBlank(personDateOfBirth)){
			personAttributes.put(IdentifierGenerationStrategy.BIRTHDATE_FIELD, personDateOfBirth);
		}	
				
		String personSex=XmlUtils.xPathStringSearch(personNode, "nc30:PersonSexText");
		if (StringUtils.isNotBlank(personSex)){
			personAttributes.put(IdentifierGenerationStrategy.SEX_FIELD, personSex.trim());
		}
				
		String	personSsn = XmlUtils.xPathStringSearch(personNode, "nc30:PersonSSNIdentification/nc30:IdentificationID");
		if (StringUtils.isNotBlank(personSsn)){
			personAttributes.put(IdentifierGenerationStrategy.SSN_FIELD, personSsn.trim());
		}
		
		log.debug("Person Attribute map: " + personAttributes);
		return personAttributes;
	}

	public IdentifierGenerationStrategy getIdentifierGenerationStrategy() {
		return identifierGenerationStrategy;
	}

	public void setIdentifierGenerationStrategy(
			IdentifierGenerationStrategy identifierGenerationStrategy) {
		this.identifierGenerationStrategy = identifierGenerationStrategy;
	}

}
