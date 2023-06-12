package com.facetofacegames.base;



import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Listeners({ com.facetofacegames.base.TestListener.class }) // This line lets every test that implements BaseTest use the TestNG listener (TestListener.java)
public class BaseTest {
	
	protected WebDriver driver;
	protected Logger log;
	
	protected String testSuiteName;
	protected String testName;
	protected String testMethodName;

	@Parameters({ "browser", "chromeProfile", "deviceName" })
	@BeforeSuite(alwaysRun = true)
	public void setUp(Method method, @Optional("chrome") String browser, @Optional String profile, @Optional String deviceName, ITestContext ctx) {
		String testName = ctx.getCurrentXmlTest().getName();
		log = LogManager.getLogger(testName);
		
		BrowserDriverFactory factory = new BrowserDriverFactory(browser, log);
		if (profile != null) {
			driver = factory.createChromeWithProfile(profile);
		} else if (deviceName != null) {
			driver = factory.createChromeWithMobileEmulation(deviceName);
		} else {
			driver = factory.createDriver();
		}
		
		//BrowserDriverFactory factory = new BrowserDriverFactory(browser, log);
		//driver = factory.createDriver();
		
		driver.manage().window().maximize();
		
		this.testSuiteName = ctx.getSuite().getName();
		this.testName = testName;
		//this.testMethodName = method.getName();
	}
	
	@AfterSuite(alwaysRun = true)
	public void tearDown() {
		log.info("Close driver");
		// Close browser
		driver.quit();
	}
}
