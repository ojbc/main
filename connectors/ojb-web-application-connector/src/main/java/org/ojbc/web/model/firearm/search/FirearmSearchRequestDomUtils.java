package org.ojbc.web.model.firearm.search;

import org.apache.commons.lang.StringUtils;
import org.ojbc.util.helper.NIEMXMLUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.namespaces.NIEMNamespaces;
import org.ojbc.util.xml.namespaces.OJBNamespaces;
import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.util.OJBCWebApplicationNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FirearmSearchRequestDomUtils {
	
	public static Element createFirearmSearchRequestElement(Document doc) {	
		
		Element element = doc.createElementNS(OJBCWebApplicationNamespaceContext.FIREARM_SEARCH_REQUEST, "FirearmSearchRequest");
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
			
			Element catCodeNode = XmlUtils.appendElement(firearmElement, NIEMNamespaces.NC_20_NS, "FirearmCategoryCode");		
			catCodeNode.setTextContent(fsr.getFirearmType());
		}	
		
		if (StringUtils.isNotBlank(fsr.getFirearmMake())){
			
			Element makeTxtNode = XmlUtils.appendElement(firearmElement, OJBNamespaces.FIREARM_SEARCH_REQUEST_EXT, "FirearmMakeText");
			makeTxtNode.setTextContent(fsr.getFirearmMake());			
		}
			
		doc.appendChild(firearmElement);
		
		return firearmElement;
	}
}
