package com.facetofacegames.pages;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LandingPage extends BasePageObject{
	
	private String pageUrl = "https://www.facetofacegames.com/";
	
	private By searchField = By.xpath(("//input[@type='text']"));
	//private By actualSearchField = By.xpath("(//input[@type='text'])[0]");
	//private By actualSearchButton = By.xpath("(//span[@class='search-submit'])[0]");

	public LandingPage(WebDriver driver, Logger log) {
		super(driver, log);
	}
	
	public void openPage() {
		log.info("Opening page: " + pageUrl);
		driver.get(pageUrl); // Passed from BasePageObject
		log.info("Page opened!");
	}

	public void searchCard(String cardName) {
		List<WebElement> listSearchField = driver.findElements(By.xpath("//input[@type='text']"));
		WebElement actualSearchField = listSearchField.get(0);
		List<WebElement> listSearchButton = driver.findElements(By.xpath("//span[@class='search-submit']"));
		WebElement actualSearchButton = listSearchButton.get(0);
		
		actualSearchField.sendKeys(cardName);
		actualSearchButton.click();
		
		// Normal F2F search speed is REALLY slow, so this wait was included in this search function
		By homeText = By.xpath("//span[@class='breadcrumb-label']");
		waitForVisibilityOf(homeText, Duration.ofSeconds(5));
	}
}
