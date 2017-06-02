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
package functional.org.ojbc.web.portal.controllers;

import static functional.org.ojbc.web.portal.controllers.TestBase.getUrl;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

//Simple tests that we can run with HtmlUnit
public class SimplePortalFunctionalTest {

	private WebDriver driver;

	@Before
	public void setup() {
		driver = new HtmlUnitDriver();
	}

	@Test
	public void siteIsUp() {

		driver.get(getUrl("/portal/index"));
		assertThat(driver.getTitle(), is("OJBC Search"));

	}

	@Test
	public void systemsToQueryShowsUp() {
		driver.get(getUrl("/portal/index"));

		assertThat(driver.findElement(By.id("checkbox-Criminal History")).getText(), Matchers.is("Criminal History"));
		assertThat(driver.findElement(By.id("checkbox-Warrants")).getText(), Matchers.is("Warrants"));
	}

	@Test
	public void baseUrlRedirectsToSimpleSearch() {
		driver.get(getUrl(""));
		assertThat(driver.getTitle(), is("OJBC Search"));
	}
}
