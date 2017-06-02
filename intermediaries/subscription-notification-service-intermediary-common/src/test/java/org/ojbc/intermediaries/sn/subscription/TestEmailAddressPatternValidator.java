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
package org.ojbc.intermediaries.sn.subscription;

import java.util.ArrayList;
import java.util.List;

import org.ojbc.intermediaries.sn.util.EmailAddressValidatorResponse;

import org.junit.Assert;
import org.junit.Test;

public class TestEmailAddressPatternValidator {

	@Test
	public void testAreEmailAddressesValidNoPatternSpecified()
	{
		EmailAddressPatternValidator emailDomainProcessor = new EmailAddressPatternValidator();
		
		List<String> emailAddresses = new ArrayList<String>();
		emailAddresses.add("1@1.com");
		emailAddresses.add("2@2.com");
		
		EmailAddressValidatorResponse addressValidatorResponse = emailDomainProcessor.areEmailAddressesValid(emailAddresses);
		
		Assert.assertTrue(addressValidatorResponse.isAreAllEmailAddressValid());
	}

	@Test
	public void testAreEmailAddressesValidSuccess()
	{
		EmailAddressPatternValidator emailDomainProcessor = new EmailAddressPatternValidator("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(1.com|2.com)");
		
		List<String> emailAddresses = new ArrayList<String>();
		emailAddresses.add("1@1.com");
		emailAddresses.add("2@2.com");

		EmailAddressValidatorResponse addressValidatorResponse = emailDomainProcessor.areEmailAddressesValid(emailAddresses);
		
		Assert.assertTrue(addressValidatorResponse.isAreAllEmailAddressValid());
	}

	@Test
	public void testAreEmailAddressesValidSuccessLocalhost()
	{
		EmailAddressPatternValidator emailDomainProcessor = new EmailAddressPatternValidator("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@localhost.local");
		
		List<String> emailAddresses = new ArrayList<String>();
		emailAddresses.add("testimap@localhost.local");

		EmailAddressValidatorResponse addressValidatorResponse = emailDomainProcessor.areEmailAddressesValid(emailAddresses);
		
		Assert.assertTrue(addressValidatorResponse.isAreAllEmailAddressValid());
	}

	@Test
	public void testAreEmailAddressesValidFailureOne()
	{
		EmailAddressPatternValidator emailDomainProcessor = new EmailAddressPatternValidator("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(1.com|2.com)");
		
		List<String> emailAddresses = new ArrayList<String>();
		emailAddresses.add("1@1.com");
		emailAddresses.add("2@3.com");
		
		EmailAddressValidatorResponse addressValidatorResponse = emailDomainProcessor.areEmailAddressesValid(emailAddresses);

		Assert.assertFalse(addressValidatorResponse.isAreAllEmailAddressValid());
		
		//Remove the valid email address from the list and then compare with the return value
		emailAddresses.remove("1@1.com");
		Assert.assertEquals(emailAddresses,addressValidatorResponse.getInvalidEmailAddresses() );
	}

	@Test
	public void testAreEmailAddressesValidFailureBoth()
	{
		EmailAddressPatternValidator emailDomainProcessor = new EmailAddressPatternValidator("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(1.com|2.com)");
		
		List<String> emailAddresses = new ArrayList<String>();
		emailAddresses.add("1@4.com");
		emailAddresses.add("2@3.com");

		EmailAddressValidatorResponse addressValidatorResponse = emailDomainProcessor.areEmailAddressesValid(emailAddresses);
		
		Assert.assertFalse(addressValidatorResponse.isAreAllEmailAddressValid());
		Assert.assertEquals(emailAddresses,addressValidatorResponse.getInvalidEmailAddresses() );
	}
	
	@Test
	public void testHawaiiEmailPatternValidator()
	{
		//Set up email validation pattern
		EmailAddressPatternValidator emailDomainProcessor = new EmailAddressPatternValidator("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@honolulu.gov,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@mpd.net,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@hawaiicounty.gov,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@kauai.gov,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@co.maui.hi.us,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@co.hawaii.hi.us,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@hawaii.gov,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@courts.hawaii.gov,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@hcjdc.hawaii.gov,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@dod.hawaii.gov,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@scd.hawaii.gov");
		
		List<String> emailAddresses = new ArrayList<String>();
		emailAddresses.add("bkushima@honolulu.gov");

		EmailAddressValidatorResponse addressValidatorResponse = emailDomainProcessor.areEmailAddressesValid(emailAddresses);
		
		//test one valid email
		Assert.assertTrue(addressValidatorResponse.isAreAllEmailAddressValid());
		
		emailAddresses.clear();
		emailAddresses.add("bkushima@invalid.gov");
		emailAddresses.add("bkushima@co.hawaii.hi.us");
		
		addressValidatorResponse = emailDomainProcessor.areEmailAddressesValid(emailAddresses);
		
		emailAddresses.remove("bkushima@co.hawaii.hi.us");
		
		//Test one valid and one invalid email
		Assert.assertFalse(addressValidatorResponse.isAreAllEmailAddressValid());
		Assert.assertEquals(emailAddresses,addressValidatorResponse.getInvalidEmailAddresses() );

		
		emailAddresses.clear();
		emailAddresses.add("bkushima@invalid.net");
		emailAddresses.add("bkushima@none.gov");
		
		addressValidatorResponse = emailDomainProcessor.areEmailAddressesValid(emailAddresses);
		
		//Test two invalid email addresses
		Assert.assertFalse(addressValidatorResponse.isAreAllEmailAddressValid());
		Assert.assertEquals(emailAddresses,addressValidatorResponse.getInvalidEmailAddresses() );

		emailAddresses.clear();
		emailAddresses.add("bkushima@mpd.net");
		emailAddresses.add("bkushima@co.hawaii.hi.us");
		
		addressValidatorResponse = emailDomainProcessor.areEmailAddressesValid(emailAddresses);
		
		//Test two valid email addressses
		Assert.assertTrue(addressValidatorResponse.isAreAllEmailAddressValid());
	}
	
	@Test
	public void testVermontEmailPatternValidator()
	{
		//Set up email validation pattern
		EmailAddressPatternValidator emailDomainProcessor = new EmailAddressPatternValidator("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@sbpdvt.org,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@usdoj.gov,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@usss.dhs.gov,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@windhamsheriff.com,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@royaltonpd.org,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@hartford-vt.org,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@essex.org,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@miltonvt.org,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@orangecountysheriff.com,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@winooskipolice.com,^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@state.vt.us");
		
		List<String> emailAddresses = new ArrayList<String>();
		emailAddresses.add("test@sbpdvt.org");
		emailAddresses.add("test@usdoj.gov");
		emailAddresses.add("test@usss.dhs.gov");
		emailAddresses.add("test@windhamsheriff.com");
		emailAddresses.add("test@royaltonpd.org");
		emailAddresses.add("test@hartford-vt.org");
		emailAddresses.add("test@essex.org");
		emailAddresses.add("test@miltonvt.org");
		emailAddresses.add("test@orangecountysheriff.com");
		emailAddresses.add("test@winooskipolice.com");
		emailAddresses.add("test@state.vt.us");

		EmailAddressValidatorResponse addressValidatorResponse = emailDomainProcessor.areEmailAddressesValid(emailAddresses);
		
		//test one valid email from each domain
		Assert.assertTrue(addressValidatorResponse.isAreAllEmailAddressValid());
		
	}
}
