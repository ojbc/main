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

}
