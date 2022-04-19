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
package org.ojbc.bundles.adapters.fbi.ebts;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.apache.http.Consts;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAOImpl;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackNotification;
import org.ojbc.bundles.adapters.fbi.ebts.application.FbiElectronicBiometricTransmissionSpecificationAdapterApplication;
import org.ojbc.util.camel.helper.OJBUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


@UseAdviceWith
@CamelSpringBootTest
@SpringBootTest(classes=FbiElectronicBiometricTransmissionSpecificationAdapterApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
public class CamelContextTestRapbackNotifications {
	
	private static final Log logger = LogFactory.getLog(CamelContextTestRapbackNotifications.class);
	    
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate producerTemplate;
    
    @Resource
    private EnhancedAuditDAOImpl enhanceAuditDaoImpl;
    	
    @BeforeEach
    public void setup() throws Exception{
    	
		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
    	
    	AdviceWith.adviceWith(context, "fbiEbtsProcessingRoute", route -> {
    		route.mockEndpointsAndSkip("cxf:bean:criminalHistoryUpdateReportingService*");
    	});
    	
    	context.start();	
    }
    
	@Test
	public void contextStartup() {
		Assert.assertTrue(true);
	}
	
	
	@Test
	public void processRBNConsolidation() throws Exception{
		
    	Exchange senderExchange = createSenderExchange("src/test/resources/input/FBI_RBN_UCN_Consolidation.xml");
	    producerTemplate.send("direct:processFbiNgiResponse", senderExchange);

	    Thread.sleep(1000);
	    
	    List<FederalRapbackNotification> federalRapbackNotifications = enhanceAuditDaoImpl.retrieveFederalNotifications(LocalDate.now().minusDays(1), LocalDate.now());
	    
	    assertEquals(1, federalRapbackNotifications.size());

    	senderExchange = createSenderExchange("src/test/resources/input/FBI_RBN_UCN_Deletion.xml");
	    producerTemplate.send("direct:processFbiNgiResponse", senderExchange);

	    Thread.sleep(1000);
	    
	    federalRapbackNotifications = enhanceAuditDaoImpl.retrieveFederalNotifications(LocalDate.now().minusDays(1), LocalDate.now());
	    
	    assertEquals(2, federalRapbackNotifications.size());
	    
    	senderExchange = createSenderExchange("src/test/resources/input/FBI_RBN_UCN_Restoration.xml");
	    producerTemplate.send("direct:processFbiNgiResponse", senderExchange);

	    Thread.sleep(1000);
	    
	    federalRapbackNotifications = enhanceAuditDaoImpl.retrieveFederalNotifications(LocalDate.now().minusDays(1), LocalDate.now());
	    
	    assertEquals(3, federalRapbackNotifications.size());

    	senderExchange = createSenderExchange("src/test/resources/input/FBI_Rapback_Activity_Notification.xml");
	    producerTemplate.send("direct:processFbiNgiResponse", senderExchange);

	    Thread.sleep(1000);
	    
	    federalRapbackNotifications = enhanceAuditDaoImpl.retrieveFederalNotifications(LocalDate.now().minusDays(1), LocalDate.now());
	    
	    assertEquals(4, federalRapbackNotifications.size());
	}

	private Exchange createSenderExchange(String pathToFile) throws IOException, Exception,
			ParserConfigurationException {
		Exchange senderExchange = new DefaultExchange(context);

	    File inputFile = new File(pathToFile);
	    String inputStr = FileUtils.readFileToString(inputFile, Consts.UTF_8);
	    
	    Assert.assertNotNull(inputStr);
	    
	    logger.info(inputStr);
	    
	    senderExchange.getIn().setBody(inputStr);	    
	    	    
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);				
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader("http://www.w3.org/2005/08/addressing", "MessageID", "12345"));		
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		return senderExchange;
	}
	
	
	private SoapHeader makeSoapHeader(String namespace, String localName, String value) throws ParserConfigurationException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().newDocument();
		
		Element messageId = doc.createElementNS(namespace, localName);
		messageId.setTextContent(value);
		SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
		return soapHeader;
	}	

}
