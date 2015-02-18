package org.ojbc.util.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.NamespaceContext;

/**
 * A convenience NamespaceContext implementation that takes namespace declarations from the top of an XML document and includes those in the context.
 *
 */
public class DeclarationNamespaceContext implements NamespaceContext {
    
    private Map<String, String> prefixToUriMap;
    private Map<String, String> uriToPrefixMap;
    
    public DeclarationNamespaceContext(String declarationString) {
        prefixToUriMap = new HashMap<String, String>();
        uriToPrefixMap = new HashMap<String, String>();
        Pattern p = Pattern.compile("xmlns:(.+?)=\"(.+?)\"[\\s]??");
        Matcher m = p.matcher(declarationString);
        while (m.find()) {
            String prefix = m.group(1);
            String uri = m.group(2);
            prefixToUriMap.put(prefix, uri);
            uriToPrefixMap.put(uri, prefix);
        }
    }

    @Override
    public String getNamespaceURI(String prefix) {
        return prefixToUriMap.get(prefix);
    }

    @Override
    public String getPrefix(String uri) {
        return uriToPrefixMap.get(uri);
    }

    @Override
    public Iterator getPrefixes(String uri) {
        List<String> l = new ArrayList<String>();
        l.add(getPrefix(uri));
        return l.iterator();
    }

}
