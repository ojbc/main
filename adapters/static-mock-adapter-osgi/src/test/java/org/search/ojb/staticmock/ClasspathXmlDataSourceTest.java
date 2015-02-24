package org.search.ojb.staticmock;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

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
