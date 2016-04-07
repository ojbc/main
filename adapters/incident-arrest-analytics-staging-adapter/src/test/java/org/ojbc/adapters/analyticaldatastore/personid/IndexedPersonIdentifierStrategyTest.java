package org.ojbc.adapters.analyticaldatastore.personid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.lucene.personid.IdentifierGenerationStrategy;
import org.ojbc.util.lucene.personid.IndexedIdentifierGenerationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml"
		})
public class IndexedPersonIdentifierStrategyTest {

	@Autowired
	private IndexedPersonIdentifierStrategy indexedIdentifierGenerationStrategy;
	
	private Map<String, Object> attributeMap;

	@Before
	public void setUp() throws Exception {
		attributeMap = new HashMap<String, Object>();
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.FIRST_NAME_FIELD, "George");
		attributeMap.put(IndexedIdentifierGenerationStrategy.LAST_NAME_FIELD, "Washington");
		attributeMap.put(IndexedIdentifierGenerationStrategy.MIDDLE_NAME_FIELD, "Herbert");
		attributeMap.put(IndexedIdentifierGenerationStrategy.SEX_FIELD, "M");
		attributeMap.put(IndexedIdentifierGenerationStrategy.SSN_FIELD, null);
		attributeMap.put(IndexedIdentifierGenerationStrategy.BIRTHDATE_FIELD, makeDate(1745, 2, 3));
	}

	@Test
	public void testBackup() throws Exception {
		
		String id = indexedIdentifierGenerationStrategy.generateIdentifier(attributeMap);
		
		attributeMap.put(IndexedIdentifierGenerationStrategy.SEX_FIELD, null);
		String id2 = indexedIdentifierGenerationStrategy.generateIdentifier(attributeMap);
		assertEquals(id, id2);
		
		String backupDirectoryPath = indexedIdentifierGenerationStrategy.backup();

		File backupDirectory = new File(backupDirectoryPath);
		
		assertTrue(backupDirectory.exists());
		assertTrue(backupDirectory.list().length>0);
		
		if (backupDirectory.exists())
		{	
			FileUtils.deleteDirectory(backupDirectory);
		}	
	}
	
	private Object makeDate(int year, int monthOfYear, int dayOfMonth) {
		LocalDate today = LocalDate.of(year, monthOfYear, dayOfMonth);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		String dateTimeStamp = today.format(dtf);
		
		return dateTimeStamp;
	}
}
