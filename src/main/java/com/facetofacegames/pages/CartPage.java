package com.facetofacegames.pages;

import java.io.FileReader;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;

import com.opencsv.CSVReader;

public class CartPage extends BasePageObject{

	private String pageUrl = "https://buylist.facetofacegames.com/cart.php";
	
	public CartPage(WebDriver driver, Logger log) {
		super(driver, log);
	}
	
	public void openPage() {
		log.info("Opening page: " + pageUrl);
		driver.get(pageUrl); // Passed from BasePageObject
		log.info("Page opened!");
	}
	
	
}
