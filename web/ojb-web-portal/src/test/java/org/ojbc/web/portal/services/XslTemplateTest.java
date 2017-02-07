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
package org.ojbc.web.portal.services;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.web.WebUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.xml.sax.SAXException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
        "classpath:dispatcher-servlet.xml",
        "classpath:application-context.xml",
        "classpath:static-configuration-demostate.xml", "classpath:security-context.xml"
        })
@ActiveProfiles("standalone")
@DirtiesContext
public class XslTemplateTest {
	
    private static final String CURRENT_DATE_yyyyMMdd = DateTime.now().toString("yyyy-MM-dd");
    
    private static final String CURRENT_DATE_MMddyyyy = DateTime.now().toString("MM/dd/yyyy");	

    @Resource
    SearchResultConverter searchResultConverter;
    
    private final Log logger = LogFactory.getLog(this.getClass());

    @Before
    public void setup() {
    	
    	XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    }    

    
    @Test
    public void custodyDetailTest() throws IOException{
    	validatePersonSearchTransformation("xsl/custody-detail.xsl", "CustodyDetails.xml", "CustodyDetails.html");
    }
    
    @Test
    public void courtCaseListTest() throws Exception {
        validatePersonSearchTransformation("xsl/court-case-list.xsl", "courtCaseSearchResults.xml", "courtCaseSearchResults.html");
    }

    @Test
    public void courtCaseDetailTest() throws Exception {
    	validatePersonSearchTransformation("xsl/court-case-detail.xsl", "courtCaseQueryResults.xml", "courtCaseQueryResults.html");
    }
    
    @Test
    public void firearmPurchaseProhibitionDetailTest() throws Exception {
    	validatePersonSearchTransformation("xsl/firearm-purchase-prohibition.xsl", "FirearmPurchaseProhibitionQueryResullts-maine.xml", "firearmPurchaseProhibitionQueryResults.html");
    }
    
    @Test
    public void searchResultEmpty() throws Exception {
        // an empty result document should be the same whether for vehicle, person or incident search
        validatePersonSearchTransformation("xsl/personSearchResult.xsl", "searchResultEmpty.xml", "searchResultEmpty.html");
    }

 
    @Test
    public void chMultipleSentences() throws Exception {
        validatePersonSearchTransformation("xsl/criminalhistory.xsl", "criminalHistory_multiple_sentence_charge.xml", "criminalHistory_multiple_sentence_charge.html");
    }

    @Test
    public void chNoCourtCharge() throws Exception {
        validatePersonSearchTransformation("xsl/criminalhistory.xsl", "criminalHistory_no_court_charge.xml", "criminalHistory_no_court_charge.html");
    }


    @Test
    public void reOrderedFirearmSearchResult() throws Exception {
        validatePersonSearchTransformation("xsl/firearmSearchResult.xsl", "reOrderedFirearmSearchResult.xml", "reOrderedFirearmSearchResult.html");
    }

    @Test
    public void personSearchResult() throws Exception {
        validatePersonSearchTransformation("xsl/personSearchResult.xsl", "searchResult.xml", "searchResult.html");
    }

    @Test
    public void reOrderedPersonSearchResult() throws Exception {
        validatePersonSearchTransformation("xsl/personSearchResult.xsl", "reOrderedSearchResult.xml", "searchResult.html");
    }

    @Test
    public void reOrderedExpandedPersonSearchResult() throws Exception {
        validatePersonSearchTransformation("xsl/personSearchResult.xsl", "reOrderedSearchResult_with_5_entities.xml", "expandedSearchResults.html");
    }

    @Test
    public void personSearchResultTooMany() throws Exception {
        validatePersonSearchTransformation("xsl/personSearchResult.xsl", "searchResultTooMany.xml", "searchResultTooMany.html");
    }

    @Test
    public void warrantSearchDetail() throws Exception {
        validatePersonSearchTransformation("xsl/warrants.xsl", "warrants.xml", "warrants.html");
    }
    
    @Test
    public void warrantAccessDenied() throws Exception {
        validatePersonSearchTransformation("xsl/warrants.xsl", "warrants-access-denied.xml", "warrants-access-denied.html");
    }

    @Test
    public void criminalHistorySearchDetail() throws Exception {
        validatePersonSearchTransformation("xsl/criminalhistory.xsl", "criminalHistory.xml", "criminalHistory.html");
    }
    
    @Test
    public void criminalHistoryWithMultipleAddresses() throws Exception {
        validatePersonSearchTransformation("xsl/criminalhistory.xsl", "criminalHistory-multiple-addresses.xml", "criminalHistory-multiple-addresses.html");
    }

    @Test
    public void incidentPersonSearchDetail() throws Exception {
        validatePersonSearchTransformation("xsl/person-to-incidents.xsl", "personToIncident.xml", "personToIncident.html");
    }

    @Test
    public void incidentPersonSearchDetailNoRecords() throws Exception {
        validatePersonSearchTransformation("xsl/person-to-incidents.xsl", "personToIncidentNoRecords.xml", "personToIncidentNoRecords.html");
    }

    @Test
    public void incidentDetails() throws Exception {
        validatePersonSearchTransformation("xsl/incident-details.xsl", "incidents.xml", "incidents.html");
    }

    @Test
    public void incidentDetailsError() throws Exception {
        validatePersonSearchTransformation("xsl/incident-details.xsl", "incidentsError.xml", "incidentsError.html");
    }

    @Test
    public void vehicleSearchResult() throws Exception {
        validatePersonSearchTransformation("xsl/vehicleSearchResult.xsl", "vehicleSearchResult.xml", "vehicleSearchResult.html");
    }

    @Test
    public void reOrderedVehicleSearchResult() throws Exception {
        validatePersonSearchTransformation("xsl/vehicleSearchResult.xsl", "reOrderedVehicleSearchResult.xml", "vehicleSearchResult.html");
    }

    @Test
    public void firearmDetails() throws Exception {
        validatePersonSearchTransformation("xsl/firearm-details.xsl", "firearmDetails.xml", "firearmDetails.html");
        validatePersonSearchTransformation("xsl/firearm-details.xsl", "firearmDetails-demostate.xml", "firearmDetails-demostate.html");
    }

    @Test
    public void firearmDetailsText() throws Exception {
        validatePersonSearchTransformation("xsl/firearm-details.xsl", "firearmDetails_text.xml", "firearmDetails_text.html");
    }

    @Test
    public void personFirearmDetails() throws Exception {
        validatePersonSearchTransformation("xsl/firearm-details.xsl", "personFirearmDetails.xml", "personFirearmDetails.html");
        validatePersonSearchTransformation("xsl/firearm-details.xsl", "personFirearmDetails-demostate.xml", "personFirearmDetails-demostate.html");
    }

    @Test
    public void firearmDetailsWithName() throws Exception {
        validatePersonSearchTransformation("xsl/firearm-details.xsl", "firearmDetails-with-name-broken-out.xml", "personFirearmDetails.html");
    }

    @Test
    public void firearmSearchResult() throws Exception {
        validatePersonSearchTransformation("xsl/firearmSearchResult.xsl", "firearmSearchResult.xml", "firearmSearchResult.html");
        validatePersonSearchTransformation("xsl/firearmSearchResult.xsl", "firearmSearchResult-demostate.xml", "firearmSearchResult-demostate.html");
    }

    @Test
    public void firearmSearchResultText() throws Exception {
        validatePersonSearchTransformation("xsl/firearmSearchResult.xsl", "firearmSearchResult_text.xml", "firearmSearchResult_text.html");
    }

    @Test
    public void firearmSearchResultTooMany() throws Exception {
        validatePersonSearchTransformation("xsl/firearmSearchResult.xsl", "firearmSearchResultTooMany.xml", "firearmSearchResultTooMany.html");
    }

    @Test
    public void vehicleSearchResultTooMany() throws Exception {
        validatePersonSearchTransformation("xsl/vehicleSearchResult.xsl", "vehicleSearchResultTooMany.xml", "vehicleSearchResultTooMany.html");
    }

    @Test
    public void incidentVehicleSearchDetail() throws Exception {
        validatePersonSearchTransformation("xsl/vehicle-to-incidents.xsl", "vehicleToIncident.xml", "vehicleToIncident.html");
    }

    @Test
    public void incidentVehicleSearchDetailNoRecords() throws Exception {
        validatePersonSearchTransformation("xsl/vehicle-to-incidents.xsl", "vehicleToIncidentNoRecords.xml", "vehicleToIncidentNoRecords.html");
    }

    @Test
    public void incidentSearchResult() throws Exception {
        validatePersonSearchTransformation("xsl/incidentSearchResult.xsl", "incidentSearchResult.xml", "incidentSearchResult.html");
    }

    @Test
    public void reOrderedIncidentSearchResult() throws Exception {
        validatePersonSearchTransformation("xsl/incidentSearchResult.xsl", "reOrderedIncidentSearchResult.xml", "reOrderedIncidentSearchResult.html");
    }

    @Test
    public void incidentSearchResultTooMany() throws Exception {
        validatePersonSearchTransformation("xsl/incidentSearchResult.xsl", "incidentSearchResultTooMany.xml", "incidentSearchResultTooMany.html");
    }

    @Test
    public void personSearchResultEntityResSkipped() throws Exception {
        validatePersonSearchTransformation("xsl/personSearchResult.xsl", "searchResultEntityResSkipped.xml", "searchResultEntityResSkipped.html");
    }

    @Test
    public void skipER_Incident() throws Exception {
        validatePersonSearchTransformation("xsl/incidentSearchResult.xsl", "ER_Skipped.xml", "ER_Skipped_Incident.html");
    }
    
    @Test
    public void skipER_Person() throws Exception {
        validatePersonSearchTransformation("xsl/personSearchResult.xsl", "ER_Skipped.xml", "ER_Skipped_Person.html");
    }

    @Test
    public void skipER_Vehicle() throws Exception {
        validatePersonSearchTransformation("xsl/vehicleSearchResult.xsl", "ER_Skipped.xml", "ER_Skipped_Vehicle.html");
    }

    @Test
    public void skipER_Firearm() throws Exception {
        validatePersonSearchTransformation("xsl/firearmSearchResult.xsl", "ER_Skipped.xml", "ER_Skipped_Firearms.html");
    }

    @Test
    public void peopleFilter() throws Exception {
        validatePeopleFilterTransformation("xsl/personFilter.xsl", "personFilterInput.xml", "filterResult.xml");
    }
    
    @Test
    public void peopleFilterCleanupMerged() throws Exception {
        validatePeopleFilterTransformation("xsl/personFilterCleanupMerged.xsl", "personFilterCleanupMergedInput.xml", "filterCleanupMergedResult.xml");
    }
    
    @Test
    public void vehicleCrashResult() throws Exception {
        validatePersonSearchTransformation("xsl/vehicleCrashResult.xsl", "vehicleCrashResult.xml", "vehicleCrashResult.html");
    }
    
  
    @Test
    public void subscriptionSearchResult() throws Exception {    	                
                    
    	ClassPathResource xsl = new ClassPathResource("xsl/subscriptionSearchResult.xsl");
        
        String sXmlInput = IOUtils.toString(new ClassPathResource("xslTransformTest/" + "subscriptionSearchResult.xml").getInputStream());
                        
        sXmlInput = sXmlInput.replace("<nc:Date>@sub_end_date@</nc:Date>", "<nc:Date>" + CURRENT_DATE_yyyyMMdd +"</nc:Date>");
                        
        String sExpectedHtml = IOUtils.toString(new ClassPathResource("xslTransformTest/subscriptionSearchResult.html").getInputStream());
                        
        sExpectedHtml = sExpectedHtml.replace("<td>@sub_end_date@</td>", "<td>" + CURRENT_DATE_MMddyyyy + "</td>");
        
        List<String> expectedHtmlLineList = IOUtils.readLines(new ByteArrayInputStream(sExpectedHtml.getBytes()), CharEncoding.UTF_8);
                             
		// remove ojb license comment(19 lines) in memory, so it's not used in assertion
		expectedHtmlLineList.subList(0, 18).clear();
               
        searchResultConverter.searchResultXsl = xsl;
        
        String convertedHtmlPersonSearchResult = searchResultConverter.convertPersonSearchResult(sXmlInput, getPersonSearchParams());
                                    
        assertLinesEquals(expectedHtmlLineList, convertedHtmlPersonSearchResult);                                                    
    }
    
    
    
    @Test
    public void subscriptionSearchResultPastRedDates() throws Exception {
    	                
    	ClassPathResource xsl = new ClassPathResource("xsl/subscriptionSearchResult.xsl");
        
        String xmlInput = IOUtils.toString(new ClassPathResource("xslTransformTest/subscriptionSearchResult_PastRedDates.xml").getInputStream());
                
        xmlInput = xmlInput.replace("@sub_end_date@", CURRENT_DATE_yyyyMMdd);
        
        String sExpectedHtml = IOUtils.toString(new ClassPathResource("xslTransformTest/subscriptionSearchResult_PastRedDates.html").getInputStream());
                        
        sExpectedHtml = sExpectedHtml.replace("@sub_end_date@", CURRENT_DATE_MMddyyyy);
        
        List<String> expectedHtml = IOUtils.readLines(new ByteArrayInputStream(sExpectedHtml.getBytes()), CharEncoding.UTF_8);
                
		// remove ojb license comment(19 lines) in memory, so it's not used in assertion
		expectedHtml.subList(0, 18).clear();
               
        searchResultConverter.searchResultXsl = xsl;
        
        String convertResult = searchResultConverter.convertPersonSearchResult(xmlInput, getPersonSearchParams());
                               
        assertLinesEquals(expectedHtml, convertResult);
    }    
    
               
    @Test
    public void subscriptionFilterActiveSubscriptions() throws Exception{
    	
        Map<String, Object> paramsMap = new HashMap<String, Object>(3);        
        paramsMap.put("filterSubscriptionStatus", "Active");
                
        Calendar cal = Calendar.getInstance();
        cal.set(2003, 2, 1);
        Date activeDate = cal.getTime();
        
        paramsMap.put("currentDateTime", activeDate);
                
    	validateSubscriptionsFilterTransformation("xsl/subscriptionFilter.xsl", "subscriptionFilterInput.xml", "subscriptionFilterActiveResult.xml", paramsMap);    	
    }
        
    @Test
    public void subscriptionFilterInactiveSubscriptions() throws Exception{
    	
        Map<String, Object> paramsMap = new HashMap<String, Object>(3);        
        paramsMap.put("filterSubscriptionStatus", "Inactive");
                
        Calendar cal = Calendar.getInstance();
        cal.set(2001, 11, 1);
        Date inactiveDate = cal.getTime();
        
        paramsMap.put("currentDateTime", inactiveDate);
                
    	validateSubscriptionsFilterTransformation("xsl/subscriptionFilter.xsl", "subscriptionFilterInput.xml", "subscriptionFilterInactiveResult.xml", paramsMap);    	
    }
       
    @Test
    public void subscriptionFilterExpiringSubscriptions() throws Exception{
    	
        Map<String, Object> paramsMap = new HashMap<String, Object>(3);        
        paramsMap.put("filterSubscriptionStatus", "Expiring");
                
        Calendar cal = Calendar.getInstance();
        cal.set(2001, 9, 20);//9=October
        Date expiringDate = cal.getTime();
        
        paramsMap.put("currentDateTime", expiringDate);
        paramsMap.put("validationDueWarningDays", 60);
                
    	validateSubscriptionsFilterTransformation("xsl/subscriptionFilter.xsl", "subscriptionFilterInput.xml", "subscriptionGracePeriodFilterResult.xml", paramsMap);    	
    }           

    @Test
    public void subscriptionSearchResultAccessDenied() throws Exception {
        validatePersonSearchTransformation("xsl/subscriptionSearchResult.xsl", "SubscriptionSearchResults_AccessDenial.xml", "SubscriptionSearchResults_AccessDenial.html");
    }

    @Test
    public void subscriptionSearchResultError() throws Exception {
        validatePersonSearchTransformation("xsl/subscriptionSearchResult.xsl", "SubscriptionSearchResults_Error.xml", "SubscriptionSearchResults_Error.html");
    }

    @Test
    public void subscriptionSearchResultNoResults() throws Exception {
        validatePersonSearchTransformation("xsl/subscriptionSearchResult.xsl", "SubscriptionSearchResults_NoResults.xml", "SubscriptionSearchResults_NoResults.html");
    }

    @Test
    public void subscriptionSearchResultTooManyResults() throws Exception {
        validatePersonSearchTransformation("xsl/subscriptionSearchResult.xsl", "SubscriptionSearchResults_TooManyResults.xml", "SubscriptionSearchResults_TooManyResults.html");
    }
    
    
    
    @Test
    public void subscriptionSearchResultFullName() throws Exception {
    	                
    	ClassPathResource xsl = new ClassPathResource("xsl/subscriptionSearchResult.xsl");
        
        String xmlInput = IOUtils.toString(new ClassPathResource("xslTransformTest/subscriptionSearchResult_FullName.xml").getInputStream());
        
        xmlInput = xmlInput.replace("@sub_end_date@", CURRENT_DATE_yyyyMMdd);
        
        String sExpectedHtml = IOUtils.toString(new ClassPathResource("xslTransformTest/subscriptionSearchResult.html").getInputStream());
        
        sExpectedHtml = sExpectedHtml.replace("@sub_end_date@", CURRENT_DATE_MMddyyyy);
                
        List<String> expectedHtml = IOUtils.readLines(new ByteArrayInputStream(sExpectedHtml.getBytes()), CharEncoding.UTF_8);                                
                
		// remove ojb license comment(19 lines) in memory, so it's not used in assertion
		expectedHtml.subList(0, 18).clear();
               
        searchResultConverter.searchResultXsl = xsl;
        
        String convertResult = searchResultConverter.convertPersonSearchResult(xmlInput, getPersonSearchParams());
        
        logger.info("Converted Result:\n" + convertResult);
                        
        assertLinesEquals(expectedHtml, convertResult);                        
    }

    
    
    @Test
    public void rapbackSearchResult() throws Exception {
        validateRapbackSearchTransformation("OrganizationIdentificationResultsSearchResults-civil.xml", "rapbackSearchResult.html");
    }
    
    @Test
    public void rapbackErrorSearchResult() throws Exception {
        validateRapbackSearchTransformation("AccessDenial_OrganizationIdentificationResultsSearchResults.xml", "rapbackSearchAccessDenied.html");
        validateRapbackSearchTransformation("Error_OrganizationIdentificationResultsSearchResults.xml", "rapbackSearchRequestError.html");
        validateRapbackSearchTransformation("NoResults_OrganizationIdentificationResultsSearchResults.xml", "rapbackSearchNoRecord.html");
    }
    

    private void validatePeopleFilterTransformation(String xslPath, String inputXmlPath, String expectedXmlPath) 
    		throws IOException, SAXException {
    	
        ClassPathResource xsl = new ClassPathResource(xslPath);
        String xmlInput = IOUtils.toString(new ClassPathResource("xslTransformTest/" + inputXmlPath).getInputStream());        
        String expectedXml = IOUtils.toString(new ClassPathResource("xslTransformTest/" + expectedXmlPath).getInputStream());                        

        searchResultConverter.searchResultXsl = xsl;
        String convertedResult = searchResultConverter.convertPersonSearchResult(xmlInput, getFilterParams());

        Diff xmlUnitDiff = new Diff(expectedXml, convertedResult);
        
        Assert.assertTrue(xmlUnitDiff.identical());
    }
    
    
    private void validateSubscriptionsFilterTransformation(String xslPath, String inputXmlPath, String expectedXmlPath, 
    		Map<String, Object> paramsMap) throws IOException, SAXException {
    	
        ClassPathResource xsl = new ClassPathResource(xslPath);
        
        String xmlInput = IOUtils.toString(new ClassPathResource("xslTransformTest/" + inputXmlPath).getInputStream());
        
        String expectedXml = IOUtils.toString(new ClassPathResource("xslTransformTest/" + expectedXmlPath).getInputStream());                    

        searchResultConverter.subscriptionSearchResultXsl = xsl;        
                
        String convertedResult = searchResultConverter.convertSubscriptionSearchResult(xmlInput, paramsMap);
        
        logger.info("Converted Result:\n" + convertedResult);
        
        Diff xmlUnitDiff = new Diff(expectedXml, convertedResult);
                
        Assert.assertTrue(xmlUnitDiff.identical());
    }    
    

    private void validatePersonSearchTransformation(String xslPath, String inputXmlPath, String expectedHtmlPath) throws IOException {
        
    	ClassPathResource xsl = new ClassPathResource(xslPath);
        
        String xmlInput = IOUtils.toString(new ClassPathResource("xslTransformTest/" + inputXmlPath).getInputStream());
        
        List<String> expectedHtml = IOUtils.readLines(new ClassPathResource("xslTransformTest/" + expectedHtmlPath).getInputStream(), CharEncoding.UTF_8);
                
		// remove ojb license comment(19 lines) in memory, so it's not used in assertion
		expectedHtml.subList(0, 18).clear();
               
        searchResultConverter.searchResultXsl = xsl;
        
        String convertResult = searchResultConverter.convertPersonSearchResult(xmlInput, getPersonSearchParams());
        
        logger.info("Converted Result:\n" + convertResult);
                        
        assertLinesEquals(expectedHtml, convertResult);
    }
    
    private void validateRapbackSearchTransformation(String inputXmlPath, String expectedHtmlPath) throws Exception {
        
        String xmlInput = WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                "/ssp/Organization_Identification_Results_Search_Results"
                + "/artifacts/service_model/information_model/IEPD/xml/" + inputXmlPath));
        
        List<String> expectedHtml = IOUtils.readLines(new ClassPathResource("xslTransformTest/" + expectedHtmlPath).getInputStream(), CharEncoding.UTF_8);
        
        // remove ojb license comment(19 lines) in memory, so it's not used in assertion
        expectedHtml.subList(0, 18).clear();
        
        String convertResult = searchResultConverter.convertRapbackSearchResult(xmlInput);
        
        logger.info("Converted Result:\n" + convertResult);
        
        assertLinesEquals(expectedHtml, convertResult);
    }

    public Map<String, Object> getFilterParams() {

        Map<String, Object> filterParamsMap = new HashMap<String, Object>();
        
        filterParamsMap.put("filterAgeRangeStart", 0);
        filterParamsMap.put("filterAgeRangeEnd", 0);
        filterParamsMap.put("filterPersonRaceCode", "");
        filterParamsMap.put("filterPersonEyeColor", "");
        filterParamsMap.put("filterPersonHairColor", "");
        filterParamsMap.put("filterHeightInInches", 0);
        filterParamsMap.put("filterHeightInFeet", 0);
        filterParamsMap.put("filterHeightTolerance", 0);
        filterParamsMap.put("filterWeight", 0);
        filterParamsMap.put("filterWeightTolerance", 0);
        filterParamsMap.put("filterDOBStart", "");
        filterParamsMap.put("filterDOBEnd", "");
        
        return filterParamsMap;
    }

    private Map<String, Object> getPersonSearchParams() {
    	
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("start", 0);
        params.put("rows", 10);
        params.put("hrefBase", "pagination");
        params.put("validateSubscriptionButton", "true");
        params.put("messageIfNoResults", "You do not have any subscriptions.");
        return params;
    }

	private void assertLinesEquals(List<String> expectedHtml,
			String convertedResult) {
		
		String[] split = convertedResult.split("\n");

		assertThat("Lines are not equal", split.length, is(expectedHtml.size()));

		try {
			
			for (int i = 0; i < split.length; i++) {
				
				assertThat("Line " + (i + 1) + " didn't match",
						split[i].trim(), is(expectedHtml.get(i).trim()));
			}
			
		} catch (AssertionError e) {

			logger.info("------------------Converted Result: ----------------------------\n");
			logger.info(convertedResult);
			logger.info("\n----------------------------------------------");

			throw e;
		}
	}

}
