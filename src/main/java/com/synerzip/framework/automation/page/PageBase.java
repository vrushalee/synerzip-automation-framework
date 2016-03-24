/*
 * Copyright 2016 Synerzip Softech. All Rights Reserved.
 */

package com.synerzip.framework.automation.page;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

/**
 * The Class PageBase.
 */
public abstract class PageBase {

	/** The driver. */
	protected WebDriver driver = null;

	/**
	 * Instantiates a new adds the feedback page.
	 * 
	 * @param driver
	 *            the driver
	 */

	public PageBase(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	/**
	 * Wrapper method for Thread.sleep()
	 * 
	 * @param timeInSeconds
	 *            the time in seconds
	 */
	public static void wait(int timeInSeconds) {
		try {
			Thread.sleep(timeInSeconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Press enter.
	 */
	public void pressEnter() {
		try {
			Thread.sleep(5000l);
			Actions ac = new Actions(driver);
			ac.sendKeys(Keys.ENTER).perform();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

}
