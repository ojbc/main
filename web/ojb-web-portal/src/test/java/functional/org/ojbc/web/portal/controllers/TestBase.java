package functional.org.ojbc.web.portal.controllers;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBase {

	private static final int WAIT_IN_SECONDS = 15;

	public static String getUrl(String uri){
		String port = System.getProperty("serverPort", "8081");
		
		return "http://localhost:"+port+"/ojbc_web_portal" + uri;
	}
	
	public static void waitForElement(By by,WebDriver driver) {
	    WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_IN_SECONDS);
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
		} catch (Exception ex) {

		}
    }

	public static void waitForTextNotPresentFromInput(By by ,String text,WebDriver driver) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_IN_SECONDS);
		try {
			webDriverWait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(by, text)));
		} catch (Exception ex) {
			
		}
	}
	
	public static void assertElementTextIs(By by,String expectedText,WebDriver driver){
		assertThat(driver.findElement(by).getText().trim(),Matchers.is(expectedText));
	}
}
