package org.ojbc.processor.incident.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-beans-ojb-web-application-connector-context.xml" })
@ActiveProfiles(profiles={"person-search", "incident-search", "vehicle-search", "firearms-search", "person-vehicle-to-incident-search",
		"warrants-query", "criminal-history-query", "firearms-query","incident-report-query", 
		"subscriptions", "policy-acknowledgement", "access-control"})
public class IncidentSearchRequestProcessorTest {
    
    private static final Log LOG = LogFactory.getLog(IncidentSearchRequestProcessorTest.class);
    
    @Resource
    private IncidentSearchRequestProcessor processor;
    
    @Test
    @DirtiesContext
    public void testContextInitialization() throws Exception {
        assertNotNull(processor);
        assertEquals("LocationCityTownCode", processor.getCityTownCodelistElementName());
        assertEquals("http://ojbc.org/IEPD/Extensions/DemostateLocationCodes/1.0", processor.getCityTownCodelistNamespace());
    }

}
