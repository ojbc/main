package org.ojbc.util.xml;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * A DOM Resource Resolver implementation that helps with reading schemas out of IEPDs.
 *
 */
public class IEPDResourceResolver implements LSResourceResolver {
    
    private static final Log LOG = LogFactory.getLog( IEPDResourceResolver.class );
    
    protected String schemaRootFolderName;
    protected String iepdRootPath;
    
    public IEPDResourceResolver(String schemaRootFolderName, String iepdRootPath)
    {
        this.schemaRootFolderName = schemaRootFolderName;
        this.iepdRootPath = iepdRootPath;
    }

    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        String fullPath = reformatResourcePath(systemId, baseURI);
//        LOG.info("Resolving resource: type=" + type + ", namespaceURI=" + namespaceURI);
//        LOG.info("systemId=" + systemId + ", publicId=" + publicId + ", baseURI=" + baseURI);
//        LOG.info("fullPath=" + fullPath);
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
            return null;
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
            StringBuffer ret = new StringBuffer(1024 * 10);
            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            try {
                while ((line = br.readLine()) != null) {
                    ret.append(line).append("\n");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return ret.toString();
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

        private BufferedInputStream inputStream;

        public LSInputImpl(String publicId, String sysId, InputStream input) {
            this.publicId = publicId;
            this.systemId = sysId;
            this.inputStream = new BufferedInputStream(input);
        }

    }

}