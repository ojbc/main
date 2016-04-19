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
package org.springframework.ldap.samples.odm.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.samples.plain.dao.PersonDao;
import org.springframework.ldap.samples.plain.domain.Person;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration("/config/testContext.xml")
public class PersonDaoSampleIntegrationTest extends
        AbstractJUnit4SpringContextTests {

    @Autowired
    private PersonDao personDao;

    @Test
    public void testAuthenticateSuccessAndUpdatePassword() {
    	Person person = personDao.authenticate("demouser1", "password");
    	System.out.println("person: "  + person.toString());
    	assertEquals("password", person.getPassword());
    	
    	person.setPassword("newPassword");
    	personDao.update(person);
    	person = personDao.findByPrimaryKey(person.getGroup(), person.getFullName());
    	System.out.println("person after:" + person.toString());
    	System.out.println("person password after:" + person.getPassword());
    	person = personDao.authenticate("demouser1", "newPassword");
	}
    
    @Test(expected=AuthenticationException.class)
    public void testAuthenticateFailure() {
    	personDao.authenticate("demouser1", "wrongpass");
    }
}
