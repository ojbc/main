package org.search.ojb.staticmock;

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
