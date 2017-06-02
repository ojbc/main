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
