package com.facetofacegames;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.facetofacegames.base.TestUtilities;
import com.facetofacegames.pages.BuylistPage;

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
		
		BuylistPage buylistPage = new BuylistPage(driver, log);
		
		//buylistPage.provideTestData();
		
		buylistPage.openPage();
		
        
        //WebElement editionElement = driver.findElement(By.xpath("//p[@class='card-set']"));
        //List<WebElement> allCards = driver.findElements(By.xpath("//p[@class='card-set']"));
        //for(int i = 0; i < allCards.size(); i++) {
        //	System.out.println("Set is: " + allCards.get(i).getText());
        //}
        buylistPage.addToCart(quantity, name, edition, foil);
        
        System.out.println("Quantity: " + quantity);
        System.out.println("Name: " + name);
        System.out.println("Edition: " + edition);
        System.out.println("Foil: " + foil);
        sleep(1500);
       
	}

}
