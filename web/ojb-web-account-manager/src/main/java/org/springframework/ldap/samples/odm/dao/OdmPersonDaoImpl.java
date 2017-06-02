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
package org.springframework.ldap.samples.odm.dao;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapName;

import org.springframework.ldap.core.AuthenticatedLdapEntryContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapEntryIdentification;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.samples.plain.dao.PersonDao;
import org.springframework.ldap.samples.plain.domain.Person;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;

/**
 * Default implementation of PersonDao. This implementation uses the Object-Directory Mapping feature,
 * which requires the entity classes to be annotated, but relieves the programmer from the tedious
 * task of mapping to and from entity objects, using attribute or dn component values.
 * 
 */
public class OdmPersonDaoImpl implements PersonDao {

	private LdapTemplate ldapTemplate;

    @Override
	public void update(Person person) {
		ldapTemplate.update(person);
	}

    @Override
	public Person findByPrimaryKey(String group, String fullname) {
		LdapName dn = buildDn(group, fullname);
        Person person = ldapTemplate.findByDn(dn, Person.class);

        return person;
	}

	private LdapName buildDn(String group, String fullname) {
        return LdapNameBuilder.newInstance()
                .add("ou", group)
                .add("cn", fullname)
                .build();
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}


	@Override
	public Person authenticate(String uid, String password) {
		Person person  = ldapTemplate.authenticate(query().where("uid").is(uid), password, new AuthenticatedLdapEntryContextMapper<Person>() {

			@Override
			public Person mapWithContext(DirContext ctx,
					LdapEntryIdentification ldapEntryIdentification) {
			    try {
			        DirContextOperations context =  (DirContextOperations) ctx.lookup(ldapEntryIdentification.getRelativeName());
			        
					Person person = new Person();

		            LdapName dn = LdapUtils.newLdapName(context.getDn());
					person.setGroup(LdapUtils.getStringValue(dn, 0));
					person.setFullName(context.getStringAttribute("cn"));
					person.setLastName(context.getStringAttribute("sn"));
					person.setUserId(context.getStringAttribute("uid"));
					person.setMail(context.getStringAttribute("mail"));
					person.setPassword(new String((byte[])context.getObjectAttribute("userPassword")));

					return person;

			      }
			      catch (NamingException e) {
			        throw new RuntimeException("Failed to lookup " + ldapEntryIdentification.getRelativeName(), e);
			      }				
			}
		});
		
		return person;
	}
}
