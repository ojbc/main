package functional.org.ojbc.web.portal.controllers;

import static functional.org.ojbc.web.portal.controllers.TestBase.assertElementTextIs;
import static functional.org.ojbc.web.portal.controllers.TestBase.getUrl;
import static functional.org.ojbc.web.portal.controllers.TestBase.waitForElement;
import static functional.org.ojbc.web.portal.controllers.TestBase.waitForTextNotPresentFromInput;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SimpleSearchFunctionalTest {

	private WebDriver driver;

	@Before
	public void setup() {
		driver = new FirefoxDriver();
	}

	@After
	public void teardown() {
		driver.close();
	}

	@Test
	public void simpleSearchReturnsErrorWhenEmpty() throws InterruptedException {
		driver.get(getUrl("/portal/index"));
		assertThat(driver.getTitle(), is("OJBC Search"));

		waitForElement(By.id("simpleSearch"), driver);

		driver.findElement(By.id("simpleSearchSubmitButton")).click();

		waitForElement(By.className("error"), driver);
		assertElementTextIs(By.className("error"), "Search terms cannot be empty", driver);

	}

	@Test
	public void simpleSearchReturnsErrorForInvalidString() throws InterruptedException {
		driver.get(getUrl("/portal/index"));
		assertThat(driver.getTitle(), is("OJBC Search"));

		waitForElement(By.id("simpleSearch"), driver);

		driver.findElement(By.id("simpleSearch")).sendKeys("Van Halen 123-12");
		driver.findElement(By.id("simpleSearchSubmitButton")).click();
		waitForElement(By.className("error"), driver);

		assertElementTextIs(By.className("error"), "Unable to parse the following terms: [123-12]", driver);
	}

	@Test
	@Ignore("Test is unstable because of ajax calls, add more weights or something")
	public void simpleSearchClearButtonClearFields() throws InterruptedException {
		driver.get(getUrl("/portal/index"));
		assertThat(driver.getTitle(), is("OJBC Search"));

		waitForElement(By.id("simpleSearch"), driver);

		driver.findElement(By.id("simpleSearch")).sendKeys("Van Halen 123-12");
		driver.findElement(By.id("simpleSearchSubmitButton")).click();
		waitForElement(By.className("error"), driver);
		
		waitForTextNotPresentFromInput(By.id("simpleSearch"), "Search term(s)", driver);
		assertThat(driver.findElement(By.id("simpleSearch")).getAttribute("value"), is("Van Halen 123-12"));
		driver.findElement(By.id("simpleSearchClearButton")).click();
		
		WebDriverWait webDriverWait = new WebDriverWait(driver, 15);
		webDriverWait.until(not(presenceOfElementLocated(By.className("error"))));
			
		waitForElement(By.id("simpleSearch"), driver);
		assertThat(driver.findElement(By.id("simpleSearch")).getAttribute("value"), is("Search term(s)"));
	}

	@Test
	public void simplSearchSuccess() throws InterruptedException {
		driver.get(getUrl("/portal/index"));
		assertThat(driver.getTitle(), is("OJBC Search"));
		
		waitForElement(By.id("simpleSearch"), driver);
		
		driver.findElement(By.id("simpleSearch")).sendKeys("Van Halen");
		driver.findElement(By.cssSelector("input[value='{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Criminal_History/1.0}Submit-Person-Search---Criminal-History']")).click();
		driver.findElement(By.id("simpleSearchSubmitButton")).click();
		waitForElement(By.id("simpleSearch"), driver);
		
		
		waitForElement(By.id("search-results-title"), driver);
		
		
		assertThat(driver.getPageSource(),containsString("<b>Scott, Michael Gary</b>"));
	}

}
