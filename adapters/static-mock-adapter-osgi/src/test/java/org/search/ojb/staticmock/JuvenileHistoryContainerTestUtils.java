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
package org.search.ojb.staticmock;

import java.util.ArrayList;
import java.util.List;

import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class JuvenileHistoryContainerTestUtils {

	static void validateInstance(Document d, String historyComponentFolderName, String historyComponentSchemaPath) throws Exception {
		List<String> paths = new ArrayList<String>();
		paths.add("service-specifications/Juvenile_History_Services/artifacts/service_model/information_model/Juvenile_History_IEPD/" + historyComponentFolderName);
		paths.add("service-specifications/Juvenile_History_Services/artifacts/service_model/information_model/Juvenile_History_IEPD/Subset");
		paths.add("service-specifications/Juvenile_History_Services/artifacts/service_model/information_model/Juvenile_History_IEPD/Subset/niem");
		paths.add("service-specifications/Juvenile_History_Services/artifacts/service_model/information_model/Juvenile_History_IEPD/");
		XmlUtils.validateInstanceWithAbsoluteClasspaths("service-specifications/Juvenile_History_Services/artifacts/service_model/information_model/Juvenile_History_IEPD/" + historyComponentSchemaPath,
				paths, d);
	}

	public static void validateReferralHistoryDocument(Document d) throws Exception {
		validateInstance(d, "Referral", "Referral/JuvenileReferralHistoryExtension.xsd");
	}

	public static void validateOffenseHistoryDocument(Document d) throws Exception {
		validateInstance(d, "Offense", "Offense/JuvenileOffenseHistoryExtension.xsd");
	}

	public static void validateCasePlanHistoryDocument(Document d) throws Exception {
		validateInstance(d, "CasePlan", "CasePlan/JuvenileCasePlanHistoryExtension.xsd");
	}

	public static void validatePlacementHistoryDocument(Document d) throws Exception {
		validateInstance(d, "Placement", "Placement/JuvenilePlacementHistoryExtension.xsd");
	}

	public static void validateIntakeHistoryDocument(Document d) throws Exception {
		validateInstance(d, "Intake", "Intake/JuvenileIntakeHistoryExtension.xsd");
	}

	public static void validateHearingHistoryDocument(Document d) throws Exception {
		validateInstance(d, "Hearing", "Hearing/JuvenileHearingHistoryExtension.xsd");
	}

}
