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

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.ojbc.bundles.adapters.staticmock.ClasspathXmlDataSource;
import org.ojbc.bundles.adapters.staticmock.IdentifiableDocumentWrapper;

public class ClasspathXmlDataSourceTest extends TestCase {
    
    @Test
    public void testBasic() throws Exception
    {
        ClasspathXmlDataSource ds = new ClasspathXmlDataSource("ClasspathXmlDataSource-Test");
        List<IdentifiableDocumentWrapper> docs = ds.getDocuments();
        assertEquals(2, docs.size());
        boolean test1Found = false;
        boolean test2Found = false;
        for (IdentifiableDocumentWrapper d : docs) {
            if ("test1.xml".equals(d.getId()))
            {
                test1Found = true;
            }
            if ("test2.xml".equals(d.getId()))
            {
                test2Found = true;
            }
        }
        assertTrue(test1Found);
        assertTrue(test2Found);
    }
    
}
