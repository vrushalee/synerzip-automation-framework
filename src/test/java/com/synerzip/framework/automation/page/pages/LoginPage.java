/*
 * Copyright 2016 Synerzip Softech. All Rights Reserved.
 */
package com.synerzip.framework.automation.page.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.synerzip.framework.automation.page.PageBase;

/**
 * The Class LoginPage.
 */
public class LoginPage extends PageBase {

	/**
	 * Instantiates a new login page.
	 * 
	 * @param driver
	 *            the driver
	 */

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	/**
	 * Section to declare all WebElements
	 */

	@FindBy(xpath = "//*[@id='txtUsername']")
	private WebElement username;
	@FindBy(xpath = "//*[@id='txtPassword']")
	private WebElement pass;
	@FindBy(xpath = "//input[@id='btnLogin']")
	private WebElement loginButton;

	/**
	 * This method is used for Entering user name and password in their
	 * respective fields and then submit the form
	 * 
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 */
	public void loginAs(String userName, String password) {

	}
}