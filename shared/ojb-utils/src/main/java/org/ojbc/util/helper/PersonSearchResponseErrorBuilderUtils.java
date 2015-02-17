package org.ojbc.util.helper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.util.model.accesscontrol.AccessControlResponse;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class will help person search response errors using DOM
 * 
 * 
 * @author yogeshchawla
 *
 */
public class PersonSearchResponseErrorBuilderUtils {

	private static final OjbcNamespaceContext OJBC_NAMESPACE_CONTEXT = new OjbcNamespaceContext();
	
	public static Document createPersonSearchAccessDenial(AccessControlResponse accessControlResponse, String denyingSystem) throws Exception
	{
		Document doc = null;
		
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();

        doc = docBuilder.newDocument();
        Element root = doc.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_DOC, "PersonSearchResults");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_PERSON_SEARCH_RESULTS_DOC);
        
        Element searchResultsMetadata = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SEARCH_RESULTS_METADATA_EXT, "SearchResultsMetadata");
		
        Element informationAccessDenial = XmlUtils.appendElement(searchResultsMetadata, OjbcNamespaceContext.NS_IAD, "InformationAccessDenial");

        Element informationAccessDenialIndicator = XmlUtils.appendElement(informationAccessDenial, OjbcNamespaceContext.NS_IAD, "InformationAccessDenialIndicator");
        informationAccessDenialIndicator.setTextContent("true");
        
        Element informationAccessDenyingSystemNameText = XmlUtils.appendElement(informationAccessDenial, OjbcNamespaceContext.NS_IAD, "InformationAccessDenyingSystemNameText");
        informationAccessDenyingSystemNameText.setTextContent(denyingSystem);

        Element informationAccessDenialReasonText = XmlUtils.appendElement(informationAccessDenial, OjbcNamespaceContext.NS_IAD, "InformationAccessDenialReasonText");
        informationAccessDenialReasonText.setTextContent(accessControlResponse.getAccessControlResponseMessage());
        
        OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

		return doc;
	}
	

}
