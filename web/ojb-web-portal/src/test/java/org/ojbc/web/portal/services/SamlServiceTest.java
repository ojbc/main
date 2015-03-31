package org.ojbc.web.portal.services;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.is;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Element;

public class SamlServiceTest {

	private SamlService unit;
	
	@Before
	public void setUp() throws Exception {
		unit = new SamlServiceImpl();
	}

	@Test
	public void getSamlAssertion_returnsNullIfExceptionOccurs() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("Shib-Assertion-01")).thenReturn(null);
		
		Element assertion = unit.getSamlAssertion(request);
		
		assertThat(assertion, is(nullValue()));
	}

}
