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
