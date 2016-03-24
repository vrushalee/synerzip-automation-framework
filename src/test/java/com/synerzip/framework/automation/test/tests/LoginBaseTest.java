/*
 * Copyright 2016 Synerzip Softech. All Rights Reserved.
 */
package com.synerzip.framework.automation.test.tests;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.synerzip.framework.automation.test.TestBase;

/**
 * The Class LoginBaseTest.
 * 
 */
public class LoginBaseTest extends TestBase {
	/**
	 * Is WebElement Visible.
	 * 
	 * @param element
	 *            the element
	 * @return true, if successful
	 */
	public boolean isElementVisible(WebElement element) {
		// changing the implicitlyWait for this method
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		boolean elementPresent;
		try {
			elementPresent = element.isDisplayed();
		} catch (NoSuchElementException e) {
			elementPresent = false;
		}
		Long defaultWait=Long.parseLong(config.getProperty("defaultImplicitWait"));
		driver.manage().timeouts().implicitlyWait(defaultWait, TimeUnit.SECONDS);
		return elementPresent;
	}
}
