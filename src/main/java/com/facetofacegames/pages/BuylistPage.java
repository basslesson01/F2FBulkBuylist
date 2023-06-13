package com.facetofacegames.pages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class BuylistPage extends BasePageObject{

	private String pageUrl = "https://buylist.facetofacegames.com/search";
	
	private By searchField = By.xpath("//input[@class='form-input']");
	private By searchButton = By.xpath("//button[@type='submit']");
	
	private By cart = By.xpath("//div[@class='navPages-item navPages-item--cart']");
	
	// Search result of all the cards. Might not need it?
	private List<WebElement> allCards = driver.findElements(By.xpath("//li[@class='product']"));
	
	// Edition of cards
	// Since card searching is already done by searchCard(), we can assume that the result page just has the same cards from different editions
	private List<WebElement> cardEditions = driver.findElements(By.xpath("//p[@class='card-set']")); 
	
	private By conNearmint = By.xpath("//label[text()='NM']"); // Radial button for Near Mint condition cards
	private By conPlayed = By.xpath("//label[text()='PL']"); // Radial button for Played condition cards
	private By nonfoil = By.xpath("//label[text()='Non-Foil']"); // Radial button for Non-foil cards
	private By foil = By.xpath("//label[text()='Foil']"); // Radial button for Foil cards
	
	// private By quantityField = By.xpath("//div[@class='card-action']/input[@class='form-input']"); // This xpath will grab all the quantity fields for all items(cards)
	
	public BuylistPage(WebDriver driver, Logger log) {
		super(driver, log);
	}
	
	public void openPage() {
		log.info("Opening page: " + pageUrl);
		driver.get(pageUrl); // Passed from BasePageObject
		log.info("Page opened!");
	}
	
	@DataProvider(name = "csvData")
    public Object[][] provideTestData() throws Exception {
		String csvFile = "src/test/resources/test.csv";
		CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFile)).withSkipLines(1).build(); // Removes header

        List<String[]> csvData = csvReader.readAll();
        csvReader.close();

        Object[][] testData = new Object[csvData.size()][];

        for (int i = 0; i < csvData.size(); i++) {
            String[] row = csvData.get(i);
            Object[] rowData = new Object[row.length];

            for (int j = 0; j < row.length; j++) {
                rowData[j] = row[j].trim();
            }

            testData[i] = rowData;
        }

        return testData;
	}
	
	/**
	 * 	A method to parse card names with a comma in it.
	 * 	provideTestData() will treat each comma as a separate item, 
	 *  so this method is required to wrap the card names with quotation marks,
	 *  to bypass such limitation
	 *  and remove them when actually searching with the names.
	 */
	private String[] parseCSVLine(String line) {
		List<String> columns = new ArrayList<>();
	    Pattern pattern = Pattern.compile("(?<=^|,)(\"(?:[^\"]+|\"\")*\"|[^,]*)");
	    Matcher matcher = pattern.matcher(line);

	    while (matcher.find()) {
	        String column = matcher.group(1);
	        columns.add(column.replaceAll("^\"|\"$", "").replace("\"\"", "\""));
	    }

	    return columns.toArray(new String[0]);
    }
	
	public void searchCard(String cardName) {
		click(searchField);
		type(cardName, searchField);
		click(searchButton);
	}
	
	public void addToCart(String quantity, String cardName, String edition, String foil) {
		searchCard(cardName);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		
		// The for loop below is breaking the code. Need to fix
        //for(int i = 0; i < cardEditions.size(); i++) {
        	//System.out.println("addToCard is returning: " + cardEditions.get(i).getText());
        	int quant = Integer.parseInt(quantity);
        	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@class='form-label']")));
        	//if(cardEditions.get(i).getText() == edition) {
        		//System.out.println("addToCard is returning: " + cardEditions.get(i).getText());
        //	}
        //}
	}

}
