package com.facetofacegames;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.facetofacegames.base.TestUtilities;
import com.facetofacegames.pages.BuylistPage;
import com.facetofacegames.pages.CartPage;

public class UploadBuylist extends TestUtilities{
	
	//private String pageUrl = "https://buylist.facetofacegames.com/search";
	
	@DataProvider(name = "csvData")
    public Object[][] provideTestData() throws Exception {
        BuylistPage csvDataProvider = new BuylistPage(driver, log);
        return csvDataProvider.provideTestData();
    }
	
	@Test(dataProvider = "csvData")
	public void uploadBuylist(String quantity, String name, String edition, String foil) {
		
		log.info("Starting test");
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		BuylistPage buylistPage = new BuylistPage(driver, log);
		CartPage cartPage = new CartPage(driver, log);
		
		buylistPage.openPage();
		
		try {
			int csvLength = buylistPage.provideTestData().length;

			//Iterate through every item in the .csv file
			buylistPage.addToCart(quantity, name, edition, foil);
			
			//Although, it won't do the program any harm when clicking cartbutton for every item, I want it only pressed once
			//Check if this is the last item in the CSV file
	        Object[][] testData = provideTestData();
	        int currentRowIndex = getCurrentRowIndex(testData, quantity, name, edition, foil);
	        int lastRowIndex = testData.length - 1;
	        if (currentRowIndex == lastRowIndex) {
	            //Click cartButton if EOF
	            WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='navUser-item-cartLabel']")));
	            cartButton.click();
	            
	            //cartButton has to be pressed before viewCartButton
	            //Second cart Button
	            WebElement viewCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='previewCartAction-viewCart']")));
	            //viewCartButton.click();
	            cartPage.openPage();
	            sleep(3000);
	        }
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
        //System.out.println("Quantity: " + quantity);
        //System.out.println("Name: " + name);
        //System.out.println("Edition: " + edition);
        //System.out.println("Foil: " + foil);
        
		//First cart button
        //WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='navUser-item-cartLabel']")));
        //cartButton.click();
        
        //Second cart Button
        //WebElement viewCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='previewCartAction-viewCart']")));
        
	}
	
	
	// Method to get the current row index in the testData array
	private int getCurrentRowIndex(Object[][] testData, String quantity, String name, String edition, String foil) {
	    for (int i = 0; i < testData.length; i++) {
	        String testDataQuantity = (String) testData[i][0];
	        String testDataName = (String) testData[i][1];
	        String testDataEdition = (String) testData[i][2];
	        String testDataFoil = (String) testData[i][3];
	        
	        if (testDataQuantity.equals(quantity)
	            && testDataName.equals(name)
	            && testDataEdition.equals(edition)
	            && testDataFoil.equals(foil)) {
	            return i;
	        }
	    }
	    return -1; // Item not found in testData
	}

}
