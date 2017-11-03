package org.ojbc.audit.enhanced.processor;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.PersonQueryWarrantResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class TestPersonQueryWarrantProcessor {

	private static final Log log = LogFactory.getLog(TestPersonQueryWarrantProcessor.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Test
	public void testProcessPersonQueryWarrantRequest() throws Exception
	{
		AuditTestUtils.saveQueryRequest(enhancedAuditDao, "999888777666555");
		
		PersonQueryWarrantResponseSQLProcessor personQueryResponseProcessor = new PersonQueryWarrantResponseSQLProcessor();
		UserInfoSQLProcessor userInfoSQLProcessor = new UserInfoSQLProcessor();
		
		userInfoSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		personQueryResponseProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
        File inputFile = new File("src/test/resources/xmlInstances/Person_Query_Results_-_Warrants.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputFile);
		
		PersonQueryWarrantResponse personQueryWarrantResponse = personQueryResponseProcessor.processPersonQueryWarrantResponse(document);
		
		log.info(personQueryWarrantResponse.toString());
		
		assertEquals("Mickey", personQueryWarrantResponse.getFirstName());
		assertEquals("M", personQueryWarrantResponse.getMiddleName());
		assertEquals("Mouse", personQueryWarrantResponse.getLastName());
		
		assertEquals("888888888", personQueryWarrantResponse.getFbiId());
		assertEquals("999999999", personQueryWarrantResponse.getSid());
		assertEquals("Warrants", personQueryWarrantResponse.getSystemName());
		
		personQueryResponseProcessor.auditPersonQueryWarrantResponseResponse(document, "999888777666555");

	}
	
}
