package com.facetofacegames.pages;

import java.io.FileReader;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;

import com.opencsv.CSVReader;

public class BuylistLandingPage extends BasePageObject{

	private String pageUrl = "https://buylist.facetofacegames.com/";
	
	private By searchField = By.xpath("//input[@class='form-input']");
	private By searchButton = By.xpath("//button[@type='submit']");
	
	private By cart = By.xpath("//div[@class='navPages-item navPages-item--cart']");
	
	public BuylistLandingPage(WebDriver driver, Logger log) {
		super(driver, log);
	}
	
	public void openPage() {
		log.info("Opening page: " + pageUrl);
		driver.get(pageUrl); // Passed from BasePageObject
		log.info("Page opened!");
	}
	
	public void searchCard(String cardName) {
		click(searchField);
		type(cardName, searchField);
		click(searchButton);
	}
	
	@DataProvider(name = "csvData")
    public Object[][] provideTestData() throws Exception {
        String csvFile = "/selenium-practice-f2f/src/test/resources/test.csv";
        CSVReader reader = new CSVReader(new FileReader(csvFile));
        List<String[]> csvData = reader.readAll();
        reader.close();

        // Remove the header row from the CSV data
        csvData.remove(0);

        Object[][] testData = new Object[csvData.size()][4]; // Assuming 4 columns

        for (int i = 0; i < csvData.size(); i++) {
            String[] row = csvData.get(i);

            // Assign the CSV values to the test data object
            for (int j = 0; j < row.length; j++) {
                String value = row[j].replace("\"", ""); // Remove double quotes if present
                testData[i][j] = value;
            }
        }

        return testData;
    }

}
