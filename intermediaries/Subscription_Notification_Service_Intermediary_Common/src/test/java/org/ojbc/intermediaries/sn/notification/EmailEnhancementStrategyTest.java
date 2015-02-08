package org.ojbc.intermediaries.sn.notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class EmailEnhancementStrategyTest {
    
    private static final Log log = LogFactory.getLog(EmailEnhancementStrategyTest.class);

    @Test
    public void testDefaultEnhancementStrategy() {

        EmailNotification n = new EmailNotification();
        n.setSubjectName("subjectName");
        n.setSubscribingSystemIdentifier("subscribingSystemName");
        n.addToAddressee("foo@localhost");

        EmailNotification d = new DefaultEmailEnhancementStrategy().enhanceEmail(n);
        assertEquals(n, d);

    }
    
    @Test
    public void testStaticBccEmailEnhancementStrategy() {
        
        EmailNotification n = new EmailNotification();
        n.setSubjectName("subjectName");
        n.setSubscribingSystemIdentifier("subscribingSystemName");
        n.addToAddressee("foo1@localhost");

        assertTrue(n.getBccAddresseeSet().isEmpty());
        
        StaticBCCEmailEnhancementStrategy decorator = new StaticBCCEmailEnhancementStrategy();
        decorator.setBccAddress("bcc@localhost");
        
        n = decorator.enhanceEmail(n);
        
        assertTrue(n.getBccAddresseeSet().contains("bcc@localhost"));
        
        decorator = new StaticBCCEmailEnhancementStrategy();
        decorator.setBccAddress("bcc1@localhost,bcc2@localhost");
        
        n = new EmailNotification();
        n.setSubjectName("subjectName");
        n.setSubscribingSystemIdentifier("subscribingSystemName");
        n.addToAddressee("foo1@localhost");
        n = decorator.enhanceEmail(n);
        
        assertTrue(n.getBccAddresseeSet().contains("bcc1@localhost"));
        assertTrue(n.getBccAddresseeSet().contains("bcc2@localhost"));
        
        decorator = new StaticBCCEmailEnhancementStrategy();
        n = new EmailNotification();
        n.setSubjectName("subjectName");
        n.setSubscribingSystemIdentifier("subscribingSystemName");
        n.addToAddressee("foo1@localhost");
        n = decorator.enhanceEmail(n);
        assertTrue(n.getBccAddresseeSet().isEmpty());
        
    }
    
    @Test
    public void testCCEnrichmentEnhancementStrategy() {
        
        EmailNotification n = new EmailNotification();
        n.setSubjectName("subjectName");
        n.setSubscribingSystemIdentifier("subscribingSystemName");
        n.addToAddressee("foo1@localhost");
        n.addToAddressee("foo2@localhost");
        n.addCcAddressee("foo4@localhost");
        n.addBccAddressee("foo5@localhost");
        
        Map<String, String> ccLookupMap = new HashMap<String, String>();
        ccLookupMap.put("foo1@localhost", "cc1@localhost");
        ccLookupMap.put("foo3@localhost", "cc2@localhost");
        ccLookupMap.put("foo4@localhost", "cc3@localhost");
        ccLookupMap.put("foo5@localhost", "cc4@localhost");
        
        CCLookupEmailEnhancementStrategy decorator = new CCLookupEmailEnhancementStrategy();
        decorator.setCcLookupMap(ccLookupMap);
        
        n = decorator.enhanceEmail(n);
        
        assertTrue(n.getCcAddresseeSet().contains("cc1@localhost"));
        assertTrue(n.getCcAddresseeSet().contains("foo4@localhost"));
        assertFalse(n.getCcAddresseeSet().contains("cc2@localhost"));
        assertFalse(n.getCcAddresseeSet().contains("cc3@localhost"));
        assertFalse(n.getCcAddresseeSet().contains("cc4@localhost"));
        assertFalse(n.getCcAddresseeSet().contains(null));
        
    }

    @Test
    public void testWhitelistFilteringEnhancementStrategy() {

        EmailNotification n = new EmailNotification();
        n.setSubjectName("subjectName");
        n.setSubscribingSystemIdentifier("subscribingSystemName");
        n.addToAddressee("foo1@localhost");
        n.addToAddressee("foo2@localhost");
        n.addCcAddressee("foo3@localhost");
        n.addCcAddressee("foo4@localhost");
        n.addBccAddressee("foo5@localhost");
        n.addBccAddressee("foo6@localhost");

        Set<String> whitelist = new HashSet<String>();
        whitelist.add("foo1@localhost");

        WhitelistFilteringEmailEnhancementStrategy decorator = new WhitelistFilteringEmailEnhancementStrategy();
        decorator.setWhitelist(whitelist);

        EmailNotification d = decorator.enhanceEmail(n);

        assertEquals("foo1@localhost", d.getToAddresseeSet().iterator().next());
        assertEquals(1, d.getToAddresseeSet().size());
        
        assertEquals("foo2@localhost", d.getBlockedAddresseeSet().iterator().next());
        assertEquals(1, d.getBlockedAddresseeSet().size());
        
        assertEquals(2, d.getCcAddresseeSet().size());
        assertEquals(2, d.getBccAddresseeSet().size());

        whitelist.clear();
        d = decorator.enhanceEmail(n);
        assertTrue(d.getToAddresseeSet().isEmpty());
        assertEquals(2, d.getBlockedAddresseeSet().size());

    }

    @Test
    public void testCompositeEnhancementStrategy() {

        CompositeEmailEnhancementStrategy d = new CompositeEmailEnhancementStrategy();
        List<EmailEnhancementStrategy> list = new ArrayList<EmailEnhancementStrategy>();

        EmailEnhancementStrategy testEnhancementStrategy1 = new EmailEnhancementStrategy() {

            @Override
            public EmailNotification enhanceEmail(EmailNotification emailNotification) {
                EmailNotification ret = (EmailNotification) emailNotification.clone();
                ret.setSubjectName(emailNotification.getSubjectName() + "x");
                return ret;
            }
        };

        list.add(testEnhancementStrategy1);
        
        EmailEnhancementStrategy testEnhancementStrategy2 = new EmailEnhancementStrategy() {

            @Override
            public EmailNotification enhanceEmail(EmailNotification emailNotification) {
                EmailNotification ret = (EmailNotification) emailNotification.clone();
                ret.setSubjectName(emailNotification.getSubjectName() + "y");
                return ret;
            }
        };

        list.add(testEnhancementStrategy2);
        
        d.setCompositeStrategy(list);
        
        EmailNotification n = new EmailNotification();
        n.setSubjectName("subjectName");
        n.setSubscribingSystemIdentifier("subscribingSystemName");
        
        EmailNotification decorated = d.enhanceEmail(n);
        assertEquals("subjectNamexy", decorated.getSubjectName());

    }

}
