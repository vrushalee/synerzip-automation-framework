This directory contains driver executables for Internet Explorer & Chrome for different platforms - Windows/Linux/MAC and bitness - 32, 64

# Note
1. Internet Explorer is available only for Windows.
2. Chrome is only 32-bit for all platforms, so no 64-bit chrome driver.

# Downloads
ChromeDriver download location: http://chromedriver.storage.googleapis.com/index.html
IEDriver download location: http://selenium-release.storage.googleapis.com/2.44/IEDriverServer_x64_2.44.0.zip

# Launch
The driver executables should be placed in %PATH% or $PATH so that they are available for selenium 
OR,
System property need to be set for selenium to find the driver executables

System.setProperty("webdriver.ie.driver","/path/to/iedriver");
System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");