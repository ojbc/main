package org.ojbc.booking.common.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Assertions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.booking.common.dao.model.Conditions;
import org.ojbc.booking.common.dao.model.Person;
import org.ojbc.booking.common.dao.model.PersonAlias;
import org.ojbc.booking.common.dao.model.request.CustodyPersonSearchRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class CustodyDatastoreDaoPersonSearchTest {

	private SimpleDateFormat sdfyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final Log log = LogFactory.getLog( CustodyDatastoreDaoPersonSearchTest.class );
	
	@Resource
	private CustodyDatastoreDAO custodyDatastoreDao;
			
	@Before
	public void init(){
	
		Assertions.assertNotNull(custodyDatastoreDao);
	}
	
	@Test
	public void getPersonListWithPrsnSrchReqTest() throws ParseException{
	
		CustodyPersonSearchRequest prsnSrchReq = new CustodyPersonSearchRequest();
		
		prsnSrchReq.setGivenName("home");
		prsnSrchReq.setGivenNameHasStartsWithQualifier(true);
		
		prsnSrchReq.setSurNameHasStartsWithQualifier(true);
		prsnSrchReq.setSurName("simpso");
				
		prsnSrchReq.setMiddleName("jay");		
		prsnSrchReq.setSexCode("M");
		prsnSrchReq.setStateId("123");
		prsnSrchReq.setRaceCode("A");		
		prsnSrchReq.setEyeColor("XXX");		
		prsnSrchReq.setHairColor("BLK");		
		prsnSrchReq.setWeightRangeMin(149);
		prsnSrchReq.setWeightRangeMax(151);		
		prsnSrchReq.setHeightRangeMin(53);		
		prsnSrchReq.setHeightRangeMax(55);
				
		Date dob = sdfyyyyMMdd.parse("2000-01-01");
		prsnSrchReq.setDob(dob);
		
		List<Person> personList = custodyDatastoreDao.getPersonList(prsnSrchReq);
		
		Assertions.assertNotNull(personList);
		
		Assertions.assertTrue(!personList.isEmpty());

		int personSearchResultCount = personList.size();
		
		// multiple aliases in h2 mock db sample data should 
		// not cause multiple person results here
		Assertions.assertEquals(1, personSearchResultCount);		
		
		Person personSearchResult = personList.get(0);
		
		String personUniqueId = personSearchResult.getPersonUniqueIdentifier();
		
		Assertions.assertEquals("abc123", personUniqueId);	
		Assertions.assertEquals("homer", personSearchResult.getFirstName());
		
		List<PersonAlias> personAliasList = personSearchResult.getPersonAliasList();
		
		Assertions.assertNotNull(personAliasList);
		
		log.info("Person alias info: " + personAliasList);
		
		Assertions.assertEquals(2, personAliasList.size());
		
		PersonAlias pAlias0 = personAliasList.get(0);
		
		PersonAlias pAlias1 = personAliasList.get(1);
		
		List<String> aliasFNameList = Arrays.asList(pAlias0.getAliasFirstName(), pAlias1.getAliasFirstName());
		
		Assertions.assertTrue(aliasFNameList.contains("homy"));
		
		Assertions.assertTrue(aliasFNameList.contains("homie"));
		
		List<Conditions> conditionsList = personSearchResult.getConditionsList();
		
		Assertions.assertNotNull(conditionsList);
		
		log.info("Conditions List: " + conditionsList);
		
		Assertions.assertEquals(2, conditionsList.size());
		
		Conditions condition1 = conditionsList.get(0);
		
		Conditions condition2 = conditionsList.get(1);
		
		List<String> conditionsListArray = Arrays.asList(condition1.getConditionsDescription(), condition2.getConditionsDescription());
		
		Assertions.assertTrue(conditionsListArray.contains("condition 1"));
		
		Assertions.assertTrue(conditionsListArray.contains("condition 2"));
		
	}	
	
}

