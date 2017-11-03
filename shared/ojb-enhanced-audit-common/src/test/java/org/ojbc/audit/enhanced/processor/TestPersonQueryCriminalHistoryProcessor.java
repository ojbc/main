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
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class TestPersonQueryCriminalHistoryProcessor {

	private static final Log log = LogFactory.getLog(TestPersonQueryCriminalHistoryProcessor.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Test
	public void testProcessPersonQueryCriminalHistoryRequest() throws Exception
	{
		AuditTestUtils.saveQueryRequest(enhancedAuditDao, "999888777666555");
		
		PersonQueryCriminalHistoryResponseSQLProcessor personQueryResponseProcessor = new PersonQueryCriminalHistoryResponseSQLProcessor();
		UserInfoSQLProcessor userInfoSQLProcessor = new UserInfoSQLProcessor();
		
		userInfoSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		personQueryResponseProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
        File inputFile = new File("src/test/resources/xmlInstances/Criminal_History.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputFile);
		
		PersonQueryCriminalHistoryResponse personQueryCriminalHistoryResponse = personQueryResponseProcessor.processPersonQueryCriminalHistoryResponse(document);
		
		log.info(personQueryCriminalHistoryResponse.toString());
		
		assertEquals("Mickey", personQueryCriminalHistoryResponse.getFirstName());
		assertEquals("Middle", personQueryCriminalHistoryResponse.getMiddleName());
		assertEquals("Mouse", personQueryCriminalHistoryResponse.getLastName());
		
		assertEquals("12345678X9", personQueryCriminalHistoryResponse.getFbiId());
		assertEquals("A1234567", personQueryCriminalHistoryResponse.getSid());
		assertEquals("Criminal History", personQueryCriminalHistoryResponse.getSystemName());
		
		personQueryResponseProcessor.auditPersonQueryCriminalHistoryResponseResponse(document, "999888777666555");

	}
	
}
