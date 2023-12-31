package com.facetofacegames.pages;

import java.io.FileReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class BuylistPage extends BasePageObject{

	private String pageUrl = "https://buylist.facetofacegames.com/search";
	
	private By searchField = By.xpath("//input[@class='form-input']");
	private By searchButton = By.xpath("//button[@type='submit']");
	
	private By cart = By.xpath("//div[@class='navPages-item navPages-item--cart']");
	
	// Search result of all the cards. Might not need it?
	//private List<WebElement> allCards = driver.findElements(By.xpath("//li[@class='product']"));
	
	// Edition of cards
	// Since card searching is already done by searchCard(), we can assume that the result page just has the same cards from different editions
	//private List<WebElement> cardEditions = driver.findElements(By.xpath("//p[@class='card-set']")); 
	
	//private By conNearmint = By.xpath("//label[text()='NM']"); // Radial button for Near Mint condition cards
	//private By conPlayed = By.xpath("//label[text()='PL']"); // Radial button for Played condition cards
	//private By nonfoil = By.xpath("//label[text()='Non-Foil']"); // Radial button for Non-foil cards
	//private By foil = By.xpath("//label[text()='Foil']"); // Radial button for Foil cards
	
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
                //rowData[j] = row[j].trim();
            	String parsedValue = parseCSVLine(row[j].trim())[0]; // Parsing the CSV line
                rowData[j] = parsedValue;
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
	 *  With that being said, there's probably an easier way to do this.
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
		sleep(2000);
		
		// Have to be declared here because they become stale :(
		//List<WebElement> allCards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[@class='product']")));
		List<WebElement> staleEditions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//p[@class='card-set']"))); // driver.findElements(By.xpath("//p[@class='card-set']"));
		
		//By sellButton = By.xpath("//svg[@class='icon icon-sell']"); // TO DO: Find the right xpath for the Sell button. This xpath does not work.
		
        //int quant = Integer.parseInt(quantity);
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@class='form-label']")));  // The element for the word "FILTER". Just to make sure that the result page has loaded.
        for(int i = 0; i < staleEditions.size(); i++) {
        	try {
                List<WebElement> cardEditions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//p[@class='card-set']")));
                WebElement lastCard = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@class='product'][last()]")));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@class='form-label']")));

                WebElement temp = cardEditions.get(i);
                String currentEdition = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].textContent;", temp);
                System.out.println("Temp: " + currentEdition);

                if (currentEdition.equals(edition)) {

                	// This seems to work?
                	// Get the corresponding card using the index 'i'
                    WebElement card = driver.findElement(By.xpath("(//article[@data-name])[position()=" + (i + 1) + "]"));

                    String f2fAttributeValue = card.getAttribute("data-name"); // Get the specific edition's data-name attribute
                    
                    // For unknown reasons, F2F likes to add random brackets and numbers to their Editions, so the below xpath builder is needed
                    String foilXpath = "//article[@data-name='" + f2fAttributeValue + "']/div[@class='card-finish']/div[@class='finish-option item-option card-option-" + foil + "']/label";
                    System.out.println("foilXpath: " + foilXpath);
                    clickFoilType(foilXpath);
                    
                    try {
                    	// Type in the number of cards
                    	//WebElement numField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='card-action']/input[@class='form-input']")));
                    	WebElement numField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@class='card-action']/input[@class='form-input'])[" + (i + 1) + "]")));
                        //wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(numField)));

                        // Re-fetch the numField element after the refreshing is done
                        //numField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='card-action']/input[@class='form-input']")));
                        numField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@class='card-action']/input[@class='form-input'])[" + (i + 1) + "]")));
                        numField.clear();
                        numField.sendKeys(quantity);
                        
                        WebElement sellButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@type='button'])[" + (i + 1) + "]")));
                        sellButton.click();
                    	sleep(3000);
                        
                    } catch (StaleElementReferenceException e) {
                        System.out.println("Sending keys failed. Retrying...");
                        i--; // Decrement i to retry processing the same index
                    }
                    // Press the Sell button
                }
            } catch (StaleElementReferenceException e) {
                System.out.println("StaleElementReferenceException occurred. Retrying...");
                i--; // Decrement i to retry processing the same index
            }
        	
        }
		
	}
	
	public boolean clickFoilType(String xpath) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		
		boolean outcome = false;
		int retry = 0;
		
		// Face2face website is just awful. Brute force retry.
		while(retry < 10) {
			try {
				WebElement foilType = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
				foilType.click();
				outcome = true;
				//wait.until(ExpectedConditions.elementSelectionStateToBe(foilType, true));
				System.out.println("YAAAAAAAAAAAAAY");
				break;
			} catch(StaleElementReferenceException e) {
				e.printStackTrace();
			}
			retry++;
		}
		
		return outcome;
	}
	
	
	
	protected void sleep(long m) {
		try {
			Thread.sleep(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
