package org.ojbc.bundles.adapters.consentmanagement.processor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.ojbc.bundles.adapters.consentmanagement.util.ConsentManagementAdapterTestUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-consent-management-datastore.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml"
		})
public class TestConsentXMLProcessor {
	
	@Autowired
	private ConsentXMLProcessor consentXMLProcessor;

	@Test
	public void testCreateConsentReport() throws Exception
	{
		
		Consent consent = ConsentManagementAdapterTestUtils.returnConsent(2, "b1", "n1", LocalDate.now(), "First", "M", "Last", "middle", LocalDateTime.now());
		
		consent.setConsentId(1);
		consent.setConsentDecisionTimestamp(LocalDateTime.now());
		consent.setConsentUserFirstName("consent first");
		consent.setConsentUserLastName("consent last");
		consent.setConsenterUserID("Consent user ID");
		
		Document doc = consentXMLProcessor.createConsentReport(consent);
		
		XmlUtils.printNode(doc);
		
		//TODO: Add xpath tests here
	}
}
