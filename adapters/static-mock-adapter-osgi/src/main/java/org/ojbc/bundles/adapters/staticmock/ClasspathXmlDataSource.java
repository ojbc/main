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
package org.ojbc.bundles.adapters.staticmock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ClasspathXmlDataSource {

    private static final Log LOG = LogFactory.getLog(ClasspathXmlDataSource.class);

    private String directory;
    private Map<String, IdentifiableDocumentWrapper> documents;

    public ClasspathXmlDataSource(String directory) {
        this.directory = directory;
        documents = new HashMap<String, IdentifiableDocumentWrapper>();
    }

    public List<IdentifiableDocumentWrapper> getDocuments() throws Exception {
        loadDocumentMap();
        List<IdentifiableDocumentWrapper> ret = new ArrayList<IdentifiableDocumentWrapper>();
        ret.addAll(documents.values());
        return Collections.unmodifiableList(ret);
    }

    private void loadDocumentMap() throws ParserConfigurationException, IOException, SAXException {
        if (documents.isEmpty()) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            Bundle bundle = FrameworkUtil.getBundle(getClass());

            PathMatchingResourcePatternResolver resolver = null;
            if (bundle != null) {
                LOG.info("ClasspathXmlDataSource running in OSGi context, bundle=" + bundle.getSymbolicName());
                resolver = new OsgiBundleResourcePatternResolver(bundle);
            } else {
                LOG.info("ClasspathXmlDataSource running in non-OSGi context");
                resolver = new PathMatchingResourcePatternResolver();
            }
            Resource[] resources = resolver.getResources("classpath:" + directory + "/*.xml");
            LOG.info("Loaded " + resources.length + " instance files from " + directory);

            for (Resource resource : resources) {
                Document d = db.parse(resource.getInputStream());
                String filename = resource.getFilename();
                documents.put(filename, new IdentifiableDocumentWrapper(d, filename));
            }
        }
    }

    public IdentifiableDocumentWrapper getDocument(String fileName) throws Exception {
        loadDocumentMap();
        return documents.get(fileName);
    }

}
