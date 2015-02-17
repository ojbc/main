package org.ojbc.util.validator;

import org.junit.Assert;
import org.junit.Test;

public class TestEmailValidator {

	@Test
	public void testAreEmailAddressesValidNoPatternSpecified()
	{
		
		Assert.assertTrue(EmailValidator.validateEmailAddress("1@1.com"));
		Assert.assertTrue(EmailValidator.validateEmailAddress("test@localhost.local"));
		Assert.assertTrue(EmailValidator.validateEmailAddress("test@localhost"));
		Assert.assertFalse(EmailValidator.validateEmailAddress("1@1@.com"));
		Assert.assertFalse(EmailValidator.validateEmailAddress("1.com"));
		Assert.assertFalse(EmailValidator.validateEmailAddress(".com"));
		Assert.assertFalse(EmailValidator.validateEmailAddress("().com"));
		Assert.assertFalse(EmailValidator.validateEmailAddress("@.com"));
		Assert.assertFalse(EmailValidator.validateEmailAddress("1..@.com"));
		
		
	}

	@Test
	public void testAreEmailAddressesValidSuccess()
	{		
		Assert.assertTrue(EmailValidator.testEmailAddressAgainstRegularExpression("1@1.com", "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(1.com|2.com)"));
		Assert.assertFalse(EmailValidator.testEmailAddressAgainstRegularExpression("1@3.com", "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(1.com|2.com)"));
	}

	
}
