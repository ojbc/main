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
package org.ojbc.util.error.util;

import org.junit.Test;
import org.ojbc.util.helper.PersonQueryResponseErrorBuilderUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class TestPersonQueryResponseErrorBuilderUtils {
	
	@Test
	public void testCreatePersonSearchError() throws Exception{
						
		
		Document doc = PersonQueryResponseErrorBuilderUtils.createPersonQueryCriminalHistoryError("Error Text", "ID12345");
		
		//XmlUtils.printNode(doc);		

        // ensure the document we generated is valid by using the xsd to validate it
        XmlUtils.validateInstance("ssp/Criminal_History_Query_Results_Service/artifacts/service_model/information_model/Criminal_History-IEPD/xsd", 
        		"NIEM_2.1", "Criminal_History.xsd", doc);

	}	

}
