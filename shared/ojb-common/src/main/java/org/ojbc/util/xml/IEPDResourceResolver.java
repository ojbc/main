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
package org.ojbc.util.xml;

import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * A DOM Resource Resolver implementation that helps with reading schemas out of IEPDs.
 *
 */
public class IEPDResourceResolver implements LSResourceResolver {
        
    protected String schemaRootFolderName;
    protected String iepdRootPath;
    
    public IEPDResourceResolver(String schemaRootFolderName, String iepdRootPath){
    	
        this.schemaRootFolderName = schemaRootFolderName;
        this.iepdRootPath = iepdRootPath;
    }

    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
    	
        String fullPath = reformatResourcePath(systemId, baseURI);
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fullPath);
        return new LSInputImpl(publicId, systemId, resourceAsStream);
    }

    /**
     * Reformat the resource path as necessary.  The default removes "../" portions of the relative path references (since they just refer back to the root).  Subclasses
     * are free to override to do something different if needed.
     * @param systemId the unformatted relative path (from the iepd root)
     * @return the reformatted path
     */
    protected String reformatResourcePath(String systemId, String baseURI) {
    	
        String doctoredSystemId = systemId;
        if (systemId.contains("../"))
        {
            // this will work with NIEM and this specific IEPD...needs to be changed for IEPDs with other structures...
            doctoredSystemId = schemaRootFolderName + "/" + systemId.replaceAll("\\.\\./", "");
        }
        if (!iepdRootPath.endsWith("/"))
        {
            iepdRootPath = iepdRootPath + "/";
        }
        String fullPath = iepdRootPath + doctoredSystemId;
        if (fullPath.contains("/./")) {
        	fullPath = fullPath.replaceAll("/\\./", "/");
        }
        return fullPath;
    }

    public static final class LSInputImpl implements LSInput {

        private String publicId;

        private String systemId;

        public String getPublicId() {
            return publicId;
        }

        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        @Override
        public String getBaseURI() {
            return null;
        }

        @Override
        public InputStream getByteStream() {
            return inputStream;
        }

        @Override
        public boolean getCertifiedText() {
            return false;
        }

        @Override
        public Reader getCharacterStream() {
            return null;
        }

        @Override
        public String getEncoding() {
            return null;
        }

        @Override
        public String getStringData() {        	        	
        	return null;
        }

        @Override
        public void setBaseURI(String baseURI) {
        }

        @Override
        public void setByteStream(InputStream byteStream) {
        }

        @Override
        public void setCertifiedText(boolean certifiedText) {
        }

        @Override
        public void setCharacterStream(Reader characterStream) {
        }

        @Override
        public void setEncoding(String encoding) {
        }

        @Override
        public void setStringData(String stringData) {
        }

        public String getSystemId() {
            return systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }
        
        private InputStream inputStream; 

        public LSInputImpl(String publicId, String sysId, InputStream inputStream) {
            this.publicId = publicId;
            this.systemId = sysId;            
            this.inputStream = inputStream;
        }
    }
}
