package com.facetofacegames;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.facetofacegames.base.TestUtilities;
import com.facetofacegames.pages.BasePageObject;
import com.facetofacegames.pages.LandingPage;


public class InitialTests extends TestUtilities{
	
	@Test
	public void initialTest() {
		log.info("Starting initialTest");
		LandingPage landingPage = new LandingPage(driver,log);
		landingPage.openPage();
		
		String cardToSearch = "Ulamog";
		
		System.out.println("Searching for: " + cardToSearch);
		landingPage.searchCard(cardToSearch);
		
		String expectedUrlText = "https://www.facetofacegames.com/search";
		
		// Get current url
		String actualUrl = driver.getCurrentUrl();
		
		System.out.println(actualUrl);
		Assert.assertTrue(actualUrl.contains(expectedUrlText), "Page URL does not contain the expected text url: " + expectedUrlText);
		sleep(3000);
	}
	
	// For each hotel object
	// "//div[@class='uitk-spacing uitk-spacing-margin-blockstart-three']/div[@data-stid='lodging-card-responsive']"
}
