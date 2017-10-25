package org.ojbc.audit.enhanced.processor;

import static org.junit.Assert.*;

import java.io.File;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class TestPersonSearchRequestProcessor {

	private static final Log log = LogFactory.getLog(TestPersonSearchRequestProcessor.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Test
	public void testProcessPersonSearchRequest() throws Exception
	{
		PersonSearchRequestProcessor personSearchRequestProcessor = new PersonSearchRequestProcessor();
		
		personSearchRequestProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
        File inputFile = new File("src/test/resources/xmlInstances/personSearchRequest.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputFile);
		
		PersonSearchRequest personSearchRequest = personSearchRequestProcessor.processPersonSearchRequest(document);
		
		log.info(personSearchRequest.toString());
		
		assertEquals("Frank", personSearchRequest.getFirstName());
		assertEquals(new Integer(2), personSearchRequest.getFirstNameQualifier());
		assertEquals("f", personSearchRequest.getMiddleName());
		assertEquals("Smith", personSearchRequest.getLastName());
		assertEquals(new Integer(2), personSearchRequest.getLastNameQualifier());
		assertEquals("123456789", personSearchRequest.getSsn());
		assertEquals("D123456789", personSearchRequest.getDriverLicenseId());
		assertEquals("WI", personSearchRequest.getDriverLiscenseIssuer());
		assertEquals("FBI12345", personSearchRequest.getFbiNumber());
		assertEquals("A123456789", personSearchRequest.getStateId());
		assertEquals("I", personSearchRequest.getRaceCode());
		assertEquals("M", personSearchRequest.getGenderCode());
		assertEquals("BLU", personSearchRequest.getEyeCode());
		assertEquals("BLK", personSearchRequest.getHairCode());
		assertEquals(2, personSearchRequest.getSystemsToSearch().size());
		assertEquals("{system1}URI", personSearchRequest.getSystemsToSearch().get(0));
		assertEquals("{system2}URI", personSearchRequest.getSystemsToSearch().get(1));
		
	}
	
}
