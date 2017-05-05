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

import org.junit.Test;
import org.ojbc.intermediaries.sn.testutil.TestNotificationBuilderUtil;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestNotificationRequest;

public class EmailEnhancementStrategyTest {
    
    @Test
    public void testTopicMapEmailEnhancementStrategy() throws Exception {
    	
    	Map<String, EmailEnhancementStrategy> strategyTopicMap = new HashMap<String, EmailEnhancementStrategy>();
    	strategyTopicMap.put("{http://ojbc.org/wsn/topics}:person/arrest", new EmailEnhancementStrategy() {
			@Override
			public EmailNotification enhanceEmail(EmailNotification emailNotification) {
				emailNotification.setSubjectName("arrestTopicName");
				return emailNotification;
			}
		});
    	strategyTopicMap.put("{http://ojbc.org/wsn/topics}:person/incident", new EmailEnhancementStrategy() {
			@Override
			public EmailNotification enhanceEmail(EmailNotification emailNotification) {
				emailNotification.setSubjectName("incidentTopicName");
				return emailNotification;
			}
		});
    	TopicMapEmailEnhancementStrategy strategy = new TopicMapEmailEnhancementStrategy();
    	strategy.setDefaultStrategy(new EmailEnhancementStrategy() {
			@Override
			public EmailNotification enhanceEmail(EmailNotification emailNotification) {
				emailNotification.setSubjectName("defaultTopicName");
				return emailNotification;
			}
		});
    	strategy.setStrategyTopicMap(strategyTopicMap);
    	
    	EmailNotification n = new EmailNotification();
        n.setSubjectName("subjectName");
        NotificationRequest notificationRequest = new ArrestNotificationRequest(TestNotificationBuilderUtil.getMessageBody("src/test/resources/xmlInstances/notificationSoapRequest.xml"));
        n.setNotificationRequest(notificationRequest);
        
        assertEquals("arrestTopicName", strategy.enhanceEmail(n).getSubjectName());
        
        n = new EmailNotification();
        n.setSubjectName("subjectName");
        notificationRequest = new ArrestNotificationRequest(TestNotificationBuilderUtil.getMessageBody("src/test/resources/xmlInstances/notificationSoapRequest-incident.xml"));
        n.setNotificationRequest(notificationRequest);
        
        assertEquals("incidentTopicName", strategy.enhanceEmail(n).getSubjectName());
    	
        n = new EmailNotification();
        n.setSubjectName("subjectName");
        notificationRequest = new ArrestNotificationRequest(TestNotificationBuilderUtil.getMessageBody("src/test/resources/xmlInstances/notificationMessage-crimHistCycleUpdate.xml"));
        n.setNotificationRequest(notificationRequest);
        
        assertEquals("defaultTopicName", strategy.enhanceEmail(n).getSubjectName());
    	
    }
    
    @Test
    public void testDefaultEnhancementStrategy() {

        EmailNotification n = new EmailNotification();
        n.setSubjectName("subjectName");
        n.setSubscribingSystemIdentifier("subscribingSystemName");
        n.addToAddressee("foo@localhost");
        n.setSubscriptionCategoryCode("default");
        
        Map<String, String> subscriptionSubjectIdentifiers = new HashMap<String, String>();
        subscriptionSubjectIdentifiers.put("subscriptionQualifier", "123");
        
        n.setSubscriptionSubjectIdentifiers(subscriptionSubjectIdentifiers);
        
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
