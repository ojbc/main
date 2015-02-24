package org.search.ojb.staticmock;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ErrorResourceRetrieverTest {
    
    @Test
    public void testPersonSearchAccessDenied() throws Exception {
        assertNotNull(new ErrorResourceRetriever().getPersonSearchAccessDeniedDocument());
    }

}
