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
package org.ojbc.util.camel.processor.audit;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.Message;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.ojbc.util.xml.XmlUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.XMLObject;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Abstract base type for Camel processors that log exchange information to some medium.
 * 
 */
public abstract class AbstractLoggingProcessor {

    private String redactedAttributeRegex = ".^"; // default regex matches nothing, therefore nothing will be redacted
    private String redactedAttributeValue = null;

    public String getRedactedAttributeValue() {
        return redactedAttributeValue;
    }

    public void setRedactedAttributeValue(String redactedAttributeValue) {
        this.redactedAttributeValue = redactedAttributeValue;
    }

    public String getRedactedAttributeRegex() {
        return redactedAttributeRegex;
    }

    public void setRedactedAttributeRegex(String redactedAttributeRegex) {
        this.redactedAttributeRegex = redactedAttributeRegex;
    }

    protected LogInfo extractLogInfoFromExchange(Exchange e) throws UnknownHostException, Exception {

        LogInfo logInfo = new LogInfo();

        @SuppressWarnings("unchecked")
        List<SoapHeader> soapHeaders = (List<SoapHeader>) e.getIn().getHeader(Header.HEADER_LIST);
        
        if (soapHeaders != null)
        {	
	        for (SoapHeader h : soapHeaders) {
	            if (h.getName().equals(new QName("http://www.w3.org/2005/08/addressing", "From"))) {
	                logInfo.origin = ((Element) h.getObject()).getTextContent();
	            } else if (h.getName().equals(new QName("http://www.w3.org/2005/08/addressing", "To"))) {
	                logInfo.destination = ((Element) h.getObject()).getTextContent();
	            } else if (h.getName().equals(new QName("http://www.w3.org/2005/08/addressing", "MessageID"))) {
	                logInfo.messageId = ((Element) h.getObject()).getTextContent();
	            } else if (h.getName().equals(new QName("http://www.w3.org/2005/08/addressing", "ReplyTo"))) {
	                logInfo.replyTo = ((Element) h.getObject()).getTextContent();
	            }
	        }
        }    
	        
        Message message = (Message) e.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE);
        if (message != null) {
            SAMLTokenPrincipal principal = (SAMLTokenPrincipal) message.get("wss4j.principal.result");
            if (principal != null) {
                SamlAssertionWrapper aw = principal.getToken();
                if (aw != null) {
                    Assertion assertion = aw.getSaml2();

                    if (assertion != null) {

                        Pattern redactionPattern = Pattern.compile(redactedAttributeRegex);

                        for (AttributeStatement as : assertion.getAttributeStatements()) {

                            for (Attribute a : as.getAttributes()) {

                                String attributeName = a.getName();
                                List<XMLObject> attributeValues = a.getAttributeValues();

                                if (attributeValues != null && !attributeValues.isEmpty()) {

                                    XMLObject xmlObject = attributeValues.get(0);

                                    if (xmlObject != null) {

                                        Element element = xmlObject.getDOM();

                                        if (element != null) {

                                            String attributeValue = element.getTextContent();
                                            if (redactionPattern.matcher(attributeName).matches()) {
                                                attributeValue = redactedAttributeValue;
                                            }

                                            if ("gfipm:2.0:user:FederationId".equals(attributeName)) {
                                                logInfo.federationId = attributeValue;
                                            } else if ("gfipm:2.0:user:EmployerName".equals(attributeName)) {
                                                logInfo.employerName = attributeValue;
                                            } else if ("gfipm:2.0:user:EmployerSubUnitName".equals(attributeName)) {
                                                logInfo.employerSubUnitName = attributeValue;
                                            } else if ("gfipm:2.0:user:SurName".equals(attributeName)) {
                                                logInfo.userLastName = attributeValue;
                                            } else if ("gfipm:2.0:user:GivenName".equals(attributeName)) {
                                                logInfo.userFirstName = attributeValue;
                                            }

                                        }
                                    }
                                }

                            }
                        }

                        logInfo.idp = assertion.getIssuer().getValue();

                    }
                }
            }
        }

        logInfo.hostAddress = InetAddress.getLocalHost().getHostAddress();
        logInfo.camelContextId = e.getContext().getName();

        Bundle bundle = FrameworkUtil.getBundle(getClass());

        if (bundle != null) {
            logInfo.bundleName = bundle.getSymbolicName();
            logInfo.bundleVersion = bundle.getVersion().toString();
            Object bbn = bundle.getHeaders().get("Bundle-Name");
            if (bbn != null) {
                logInfo.bundleBundleName = (String) bbn;
            }
        }

        String sm = null;

        Object bodyObject = e.getIn().getBody();

        if (bodyObject instanceof String) {
            sm = (String) bodyObject;
        } else {
            Document soapBody = (Document) e.getIn().getBody(Document.class);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XmlUtils.printNode(soapBody, baos);
            sm = baos.toString();
        }
        logInfo.soapMessage = sm;
        return logInfo;
    }

    static class LogInfo {
        public Object replyTo;
        public String messageId;
        public String origin;
        public String destination;
        public String federationId;
        public String employerName;
        public String employerSubUnitName;
        public String userLastName;
        public String userFirstName;
        public String idp;
        public String hostAddress;
        public String camelContextId;
        public String soapMessage;
        public String bundleName;
        public String bundleVersion;
        public String bundleBundleName;
    }

}
