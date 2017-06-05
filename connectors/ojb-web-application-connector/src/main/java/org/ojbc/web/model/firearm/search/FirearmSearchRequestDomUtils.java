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
package org.ojbc.web.model.firearm.search;

import org.apache.commons.lang.StringUtils;
import org.ojbc.util.helper.NIEMXMLUtils;
import org.ojbc.util.xml.XmlUtils;
//import org.ojbc.util.xml.namespaces.NIEMNamespaces;
import org.ojbc.web.SearchFieldMetadata;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.ojbc.util.xml.OjbcNamespaceContext;

public class FirearmSearchRequestDomUtils {
	
	public static Element createFirearmSearchRequestElement(Document doc) {	
		
		Element element = doc.createElementNS(OjbcNamespaceContext.NS_FIREARM_SEARCH_REQUEST_DOC, "FirearmSearchRequest");
		return element;
	}
	
	private static String getMetadataStructureIdForSearchMetadata(SearchFieldMetadata searchFieldMetadata) {
		
		if (SearchFieldMetadata.ExactMatch.equals(searchFieldMetadata)) {
			return "SM001";
		} else {
			return "SM002";
		}
	}
	
	public static Element createFirearmElement(Document doc, FirearmSearchRequest fsr) {
		
		Element firearmElement = NIEMXMLUtils.createFirearmElement(doc, "FIREARM");
		
		if (StringUtils.isNotBlank(fsr.getFirearmSerialNumber())){
			
			Element itemSerialId = NIEMXMLUtils.createIdentificationElementWithStructureAttrAndParent(doc, "ItemSerialIdentification", 
					fsr.getFirearmSerialNumber(), "metadata", getMetadataStructureIdForSearchMetadata(fsr.getFirearmSerialNumberMetaData()));
			firearmElement.appendChild(itemSerialId);
		}	

		if (StringUtils.isNotBlank(fsr.getFirearmModel())){
			
			Element itemModelName = NIEMXMLUtils.createNC20Element(doc, "ItemModelName", fsr.getFirearmModel());
			firearmElement.appendChild(itemModelName);
		}			
				
		if (StringUtils.isNotBlank(fsr.getFirearmType())){
			
			Element catCodeNode = XmlUtils.appendElement(firearmElement, OjbcNamespaceContext.NS_NC, "FirearmCategoryCode");		
			catCodeNode.setTextContent(fsr.getFirearmType());
		}	
		
		if (StringUtils.isNotBlank(fsr.getFirearmMake())){
			
			Element makeTxtNode = XmlUtils.appendElement(firearmElement, OjbcNamespaceContext.NS_FIREARM_SEARCH_REQUEST_EXT, "FirearmMakeText");
			makeTxtNode.setTextContent(fsr.getFirearmMake());			
		}
			
		doc.appendChild(firearmElement);
		
		return firearmElement;
	}
}
