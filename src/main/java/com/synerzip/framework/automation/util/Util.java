/*
 * Copyright 2016 Synerzip Softech. All Rights Reserved.
 */
package com.synerzip.framework.automation.util;

import java.io.File;
import java.io.IOException;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * The Class TestUtil.
 */
public class Util {

	/**
	 * Take screen shot.
	 * 
	 * @param driver
	 *            the driver
	 * @param fileName
	 *            the file name
	 */
	public static void takeScreenShot(WebDriver driver, String fileName) {
		System.out.println("Capturing ScreenShot: ");
		File srcFile = ((TakesScreenshot) (driver))
				.getScreenshotAs(OutputType.FILE);
		//try {
		//	FileUtils.copyFile(srcFile, new File(System.getProperty("user.dir")
			//		+ "/screenshots/" + fileName + ".jpg"));
	//	} catch (IOException e) {
	//		e.printStackTrace();
	//	}
	}

}
