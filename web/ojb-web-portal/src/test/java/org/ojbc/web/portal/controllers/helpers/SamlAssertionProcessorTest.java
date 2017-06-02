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
package org.ojbc.web.portal.controllers.helpers;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class SamlAssertionProcessorTest {

    private static final Log log = LogFactory.getLog(SamlAssertionProcessorTest.class);

    private SamlTokenProcessor samlTokenProcessor;
    
    private DocumentBuilder docBuilder;

    @Before
    public void setUp() throws Exception {
    	
    	samlTokenProcessor = new SamlTokenProcessor();
        
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setNamespaceAware(true);
        docBuilder = docBuilderFactory.newDocumentBuilder();
    }

    @Test
    public void testFirearmRegRequest() throws Exception {

        File inputFile = new File("src/test/resources/saml/SampleSamlAssertion.xml");

        Document samlDoc = docBuilder.parse(inputFile);

        SamlTokenParsed samlTokenParsed = samlTokenProcessor.parseSamlToken(samlDoc);

        log.debug(samlTokenParsed);

        assertEquals("boss@police.gov", samlTokenParsed.getEmail());
    }
    


}


