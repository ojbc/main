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
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
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
