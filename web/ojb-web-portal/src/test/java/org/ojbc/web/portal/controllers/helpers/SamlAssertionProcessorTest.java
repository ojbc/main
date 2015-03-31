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


