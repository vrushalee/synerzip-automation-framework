/*
 * Copyright 2016 Synerzip Softech. All Rights Reserved.
 */
package com.synerzip.framework.automation.test;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.synerzip.framework.automation.page.pages.LoginPage;
import com.synerzip.framework.automation.util.Util;

/**
 * Test Base class implements a singleton pattern of driver instance This should
 * be extended by all test case classes
 * 
 * There are other utility methods and instance variables which can be used by
 * all extending classes.
 * 
 */

public abstract class TestBase {
	protected static WebDriver driver = null;
	protected static org.apache.log4j.Logger log = null;
	protected static Properties config = null;
	protected static Properties data = null;
	private static boolean isLoggedIn = false;
	protected static Connection conn = null;

	/**
	 * Instantiates a new test base.
	 */
	protected TestBase() {
		initLog();
		initConfig();
		initData();
		if (config.getProperty("dockerMode").equalsIgnoreCase("OFF")) {
			initDriver();
		}
		// initDB();
		// doLogin();
	}

	/**
	 * Inits the log.
	 */
	private void initLog() {
		if (log == null) {
			PropertyConfigurator.configure(
					System.getProperty("user.dir") + File.separator + "config" + File.separator + "log4j.properties");
			log = org.apache.log4j.Logger.getLogger("Logger");
			System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
			log.debug("Debug Log is initialized");
		}
	}

	/**
	 * Inits the config.
	 */
	private static void initConfig() {
		if (config == null) {
			Properties env = new Properties();
			FileInputStream ip = null;

			try {
				ip = new FileInputStream(
						System.getProperty("user.dir") + File.separator + "config" + File.separator + "env.conf");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				env.load(ip);
			} catch (IOException e) {
				e.printStackTrace();
			}

			config = new Properties();
			System.out.println("Environment = " + env.getProperty("Environment"));
			String fileName = "config_" + env.getProperty("Environment") + ".properties";
			try {
				String path = System.getProperty("user.dir") + File.separator + "config" + File.separator + fileName;
				ip = new FileInputStream(path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				config.load(ip);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void initData() {
		if (data == null) {
			Properties config_env = new Properties();
			FileInputStream ip = null;

			try {
				ip = new FileInputStream(
						System.getProperty("user.dir") + File.separator + "config" + File.separator + "env.conf");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				config_env.load(ip);
			} catch (IOException e) {
				e.printStackTrace();
			}

			data = new Properties();
			String fileName = "data_" + config_env.getProperty("Environment") + ".properties";
			try {
				ip = new FileInputStream(
						System.getProperty("user.dir") + File.separator + "config" + File.separator + fileName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				data.load(ip);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Parameters({ "browser" })
	@BeforeClass
	public void initDriver(String browser) {
		DesiredCapabilities caps = null;
		try {

			if (browser.equalsIgnoreCase("chrome")) {
				caps = DesiredCapabilities.chrome();
			} else if (browser.equalsIgnoreCase("firefox")) {
				caps = DesiredCapabilities.firefox();
			} else if (browser.equalsIgnoreCase("ie")) {
				caps = DesiredCapabilities.internetExplorer();
			}
			if (config.getProperty("platform").equalsIgnoreCase("linux")) {
				caps.setPlatform(Platform.LINUX);
			} else {
				caps.setPlatform(Platform.WINDOWS);
			}
			caps.setCapability("name", "synerzip-automation-framework");
			driver = new RemoteWebDriver(new URL(config.getProperty("hub")), caps);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (driver != null) {
			String waitTime = config.getProperty("defaultImplicitWait");
			driver.manage().timeouts().implicitlyWait(Long.parseLong(waitTime), TimeUnit.SECONDS);
			driver.manage().window().maximize();
			driver.get(config.getProperty("testsiteBaseURl"));
		} else {
			System.out.println("Driver not initialized properly!");
			System.exit(1);
		}
	}

	/**
	 * Initialize driver as per the configuration/properties file's browser type
	 */

	private static void initDriver() {
		if (driver == null) {

			if (config.getProperty("location").equalsIgnoreCase("local")) {

				System.out.println("Browser = " + config.getProperty("browser"));

				if (config.getProperty("browser").equalsIgnoreCase("firefox")) {
					String userhome = System.getProperty("user.home");
					File profileDirectory = new File((userhome + File.separator + "synerzip"));
					profileDirectory.mkdirs();
					String download = userhome + File.separator + "Downloads";
					FirefoxProfile profile = new FirefoxProfile(profileDirectory);
					profile.setPreference("browser.helperApps.alwaysAsk.force", false);
					profile.setPreference("browser.download.manager.showWhenStarting", false);
					profile.setPreference("browser.download.dir", download);
					profile.setPreference("browser.download.folderList", 2);
					profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
							"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel,application/x-excel,application/msword,application/x-rar-compressed,application/octet-stream,application/csv,text/csv,application/zip");
					// driver = new FirefoxDriver(profile);
				} else if (config.getProperty("browser").equalsIgnoreCase("ie")) {
					// Optional, if not specified, WebDriver will search your
					// path for iedriver.
					// System.setProperty("webdriver.ie.driver","/path/to/iedriver");
					// IEDriver download location:
					// http://selenium-release.storage.googleapis.com/2.44/IEDriverServer_x64_2.44.0.zip
					driver = new InternetExplorerDriver();
				} else if (config.getProperty("browser").equalsIgnoreCase("chrome")) {
					// Optional, if not specified, WebDriver will search your
					// path for chromedriver.
					// System.setProperty("webdriver.chrome.driver",
					// "/path/to/chromedriver");
					// ChromeDriver download location:
					// http://chromedriver.storage.googleapis.com/index.html
					driver = new ChromeDriver();
				} else if (config.getProperty("browser").equalsIgnoreCase("safari")) {
					// https://github.com/SeleniumHQ/selenium/wiki/SafariDriver
					// SafariDriver download location:
					// http://selenium-release.storage.googleapis.com/2.45/SafariDriver.safariextz
					// Install the driver manually
					driver = new SafariDriver();
				}
			} else if (config.getProperty("location").equalsIgnoreCase("remote")) {

				DesiredCapabilities caps = null;

				try {

					if (config.getProperty("browser").equalsIgnoreCase("chrome")) {
						caps = DesiredCapabilities.chrome();
					} else if (config.getProperty("browser").equalsIgnoreCase("firefox")) {
						caps = DesiredCapabilities.firefox();
					} else if (config.getProperty("browser").equalsIgnoreCase("ie")) {
						caps = DesiredCapabilities.internetExplorer();
					}
					caps.setCapability("name", "synerzip-automation-framework");
					driver = new RemoteWebDriver(new URL(config.getProperty("hub")), caps);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}

			if (driver != null) {
				String waitTime = config.getProperty("defaultImplicitWait");
				driver.manage().timeouts().implicitlyWait(Long.parseLong(waitTime), TimeUnit.SECONDS);
				//driver.manage().window().maximize();
				driver.get(config.getProperty("testsiteBaseURl"));
			} else {
				System.out.println("Driver not initialized properly!");
				System.exit(1);
			}
		}
	}

	/**
	 * Initialize DB connection
	 */
	private static void initDB() {
		if (conn == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				String connectionUrl = config.getProperty("connectionUrl");
				String connectionUser = config.getProperty("connectionUser");
				String connectionPassword = config.getProperty("connectionPassword");
				conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
				if (conn != null)
					log.debug("Database connection succeeded !!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Quit driver.
	 */
	public static void quitDriver() {

		driver.quit();
		driver = null;
		log.debug("Closing the Browser");
		isLoggedIn = false;
	}

	/**
	 * Gets the driver. Used to re-initialize driver in case required (example,
	 * after browser quit/crash)
	 * 
	 * @return the driver
	 */
	public static WebDriver getDriver() {
		if (driver == null) {
			initDriver();
		}
		return driver;
	}

	/**
	 * Gets the config.
	 * 
	 * @return the config
	 */
	public static Properties getConfig() {
		if (config == null) {
			initConfig();
		}
		return config;
	}

	/**
	 * Login.
	 */
	public void login() {
		if (!isLoggedIn) {
			doLogin();
			isLoggedIn = true;
		}
	}

	/**
	 * Do login. Method is used as default login as per config settings Making it
	 * private as we do not want this to be used in test cases
	 * 
	 * @return the home page
	 */
	private static void doLogin() {
		LoginPage login = new LoginPage(driver);
		login.loginAs(config.getProperty("default_UserName"), config.getProperty("default_Password"));
	}

	/**
	 * Gets the result.
	 * 
	 * @param result the result
	 * @return the result
	 */
	@AfterMethod
	public void getResult(ITestResult result) {
		System.out.println("Method Name: " + result.getMethod().getMethodName());
		System.out.println("Success Status: " + result.isSuccess());

		String run_mode = "debug";
		if (!result.isSuccess()) {
			if ((config.getProperty("mode")).equals(run_mode)) {
			} else {
				Util.takeScreenShot(driver, result.getMethod().getMethodName());
				quitDriver();
				getDriver();
				login();
			}
		}
	}

	/**
	 * The methods logs in with default user
	 */
	@BeforeMethod
	public void beforeMethodLogin() {
		login();
	}

	/**
	 * This method will quit driver after every suite execution
	 */
	@AfterSuite
	public void tearDown() {
		quitDriver();
	}

	/**
	 * Wrapper method for Thread.sleep()
	 * 
	 * @param timeInSeconds
	 */
	public static void wait(int timeInSeconds) {
		try {
			Thread.sleep(timeInSeconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wrapper method for WebDriverWait (ExpectedCondition = elementToBeClickable)
	 * 
	 * @param element
	 * @param timeInSeconds
	 */
	public static void waitForElementClickable(WebElement element, int timeInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * Wrapper method for WebDriverWait (ExpectedCondition = visibilityOf element)
	 * 
	 * @param element
	 * @param timeInSeconds
	 */
	public static void waitForElementVisible(WebElement element, int timeInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Gets the selected value in textbox.
	 * 
	 * @param textbox the textbox
	 * @return the selected value in textbox
	 */
	public String getSelectedValueInTextbox(WebElement textbox) {
		Actions builder = new Actions(driver);
		builder.moveToElement(textbox).click().perform();
		StringSelection stringSelection = new StringSelection("");
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		String selectAll = Keys.chord(Keys.CONTROL, "a");
		String ctrlC = Keys.chord(Keys.CONTROL, "c");
		textbox.sendKeys(selectAll);
		textbox.sendKeys(ctrlC);
		String selectName = "";
		try {
			selectName = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (HeadlessException e) {

			e.printStackTrace();
		} catch (UnsupportedFlavorException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return selectName;
	}

	/**
	 * Compare text Contains and gives result in true/False.
	 * 
	 * @param expected the expected
	 * @param Actual   the actual
	 * 
	 * @return boolean
	 */
	public boolean compareTextContains(String expected, String Actual) {
		boolean comapreResult;

		if (expected.contains(Actual)) {
			comapreResult = true;
		} else {
			comapreResult = false;
		}
		return comapreResult;
	}
}
