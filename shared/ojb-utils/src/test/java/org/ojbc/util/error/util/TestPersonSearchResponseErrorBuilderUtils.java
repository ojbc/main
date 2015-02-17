package org.ojbc.util.error.util;

import org.junit.Test;
import org.ojbc.util.helper.PersonSearchResponseErrorBuilderUtils;
import org.ojbc.util.model.accesscontrol.AccessControlResponse;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class TestPersonSearchResponseErrorBuilderUtils {

	@Test
	public void testCreatePersonSearchAccessDenial() throws Exception{
						
		AccessControlResponse accessControlResponse = new AccessControlResponse();
		accessControlResponse.setAuthorized(false);
		accessControlResponse.setAccessControlResponseMessage("User does not meet privilege requirements to access warrants records. To request access, contact your IT department.");
		
		Document doc = PersonSearchResponseErrorBuilderUtils.createPersonSearchAccessDenial(accessControlResponse, "Hawaii Warrants Adapter");
		
		XmlUtils.printNode(doc);		

        // ensure the document we generated is valid by using the xsd to validate it
        XmlUtils.validateInstance("service-specifications/Person_Search_Results_Service/artifacts/service_model/information_model/Person_Search_Results_IEPD/xsd", 
        		"Subset/niem", "exchange_schema.xsd", doc);

	}	

}
