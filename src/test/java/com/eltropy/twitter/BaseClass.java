package com.eltropy.twitter;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BaseClass {

	public static WebDriver driverSetup() {
		String resourcePath1 = System.getProperty("user.dir") + File.separator + "Drivers" + File.separator
				+ "geckodriver.exe";
		System.out.println(resourcePath1);
		System.setProperty("webdriver.gecko.driver", resourcePath1);
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("marionette", true);
		WebDriver driver = new FirefoxDriver(capabilities);
		return driver;
	}

}
