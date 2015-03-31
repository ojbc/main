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
