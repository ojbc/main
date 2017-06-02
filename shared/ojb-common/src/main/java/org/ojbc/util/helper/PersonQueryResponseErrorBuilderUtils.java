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
package org.ojbc.util.helper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PersonQueryResponseErrorBuilderUtils {

	private static final OjbcNamespaceContext OJBC_NAMESPACE_CONTEXT = new OjbcNamespaceContext();
	
	//	<ch-doc:CriminalHistory 
	//		xsi:schemaLocation="http://ojbc.org/IEPD/Exchange/CriminalHistory/1.0 ../xsd/Criminal_History.xsd" 
	//		xmlns:nc="http://niem.gov/niem/niem-core/2.0" 
	//		xmlns:ch-doc="http://ojbc.org/IEPD/Exchange/CriminalHistory/1.0" 
	//		xmlns:error="http://ojbc.org/IEPD/Extensions/PersonQueryErrorReporting/1.0" 
	//		>
	//		<error:PersonQueryResultError>
	//			<error:ErrorText>Error text example</error:ErrorText>
	//			<error:PersonRecordRequestIdentification>
	//				<nc:IdentificationID>PR234567</nc:IdentificationID>
	//			</error:PersonRecordRequestIdentification>
	//		</error:PersonQueryResultError>
	//	</ch-doc:CriminalHistory>
	public static Document createPersonQueryCriminalHistoryError(String errorText, String identificationID) throws Exception
	{
		Document doc = null;
		
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();

        doc = docBuilder.newDocument();
        
        Element root = doc.createElementNS(OjbcNamespaceContext.NS_CH_DOC, "CriminalHistory");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_CH_DOC);
        
        Element personQueryResultsError = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_ERROR, "PersonQueryResultError");
		
        Element errorTextElement = XmlUtils.appendElement(personQueryResultsError, OjbcNamespaceContext.NS_ERROR, "ErrorText");
        errorTextElement.setTextContent(errorText);

        Element personRecordRequestIdentification = XmlUtils.appendElement(personQueryResultsError, OjbcNamespaceContext.NS_ERROR, "PersonRecordRequestIdentification");
        
        Element identificationIDElement = XmlUtils.appendElement(personRecordRequestIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID");
        identificationIDElement.setTextContent(identificationID);
        
        OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

        return doc;
	
	}	
}
