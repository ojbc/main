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
package org.ojbc.bundles.adapters.staticmock;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.osgi.io.OsgiBundleResourcePatternResolver;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

class ErrorResourceRetriever {

    private static final Log LOG = LogFactory.getLog(ErrorResourceRetriever.class);

    public Document getPersonSearchAccessDeniedDocument() throws Exception {
        LOG.info("Returning person search access denied error results");
        return getDocument("static-error-instances/AccessDenied-PersonSearchResults.xml");
    }

    public Document getIncidentSearchAccessDeniedDocument() throws Exception {
        LOG.info("Returning incident search access denied error results");
        return getDocument("static-error-instances/AccessDenied-IncidentSearchResults.xml");
    }
    
    public Document getFirearmSearchAccessDeniedDocument() throws Exception {
        LOG.info("Returning firearm search access denied error results");
        return getDocument("static-error-instances/AccessDenied-FirearmSearchResults.xml");
    }
    
    public Document getPersonSearchErrorReportedDocument() throws Exception {
        LOG.info("Returning person search error reported results");
        return getDocument("static-error-instances/ErrorReported-PersonSearchResults.xml");
    }

    public Document getVehicleSearchAccessDeniedDocument() throws Exception {
        LOG.info("Returning vehicle search error reported results");
        return getDocument("static-error-instances/AccessDenied-VehicleSearchResults.xml");
    }

    private Document getDocument(String filename) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        PathMatchingResourcePatternResolver resolver = null;
        Resource resource = null;
        Bundle bundle = FrameworkUtil.getBundle(getClass());
        if ((bundle != null)) {
            resolver = new OsgiBundleResourcePatternResolver(bundle);
        } else {
            resolver = new PathMatchingResourcePatternResolver();
        }
        resource = resolver.getResource("classpath:" + filename);
        Document d = db.parse(resource.getInputStream());
        return d;
    }
}
