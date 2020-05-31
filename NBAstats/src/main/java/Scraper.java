import com.paulhammant.ngwebdriver.*;
import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.support.ui.Select;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scraper {
    private FirefoxDriver foxDriver;
    private FirefoxOptions options;
    private final String logFile = "log.txt";
    private NgWebDriver driver;
    private String startURL;


    public Scraper(String startURL) throws IOException {
        this.startURL = startURL;

        // initializing the web driver
        WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
        this.options = new FirefoxOptions();
        options.setHeadless(true);
        options.setLogLevel(FirefoxDriverLogLevel.ERROR);
        GeckoDriverService service = GeckoDriverService.createDefaultService();
        service.sendOutputTo(new FileOutputStream(logFile));    // redirects browser's logs to a file
        service.start();
        this.foxDriver = new FirefoxDriver(service,options);
        this.driver = new NgWebDriver(foxDriver);   // for angular

    }

    /**
     * Checks if the given string consists only of letters.
     *
     * @param player - string denoting the player's name
     */
    public boolean isNameValid(String player){
        String[] name = player.split(" ");
        if(!name[0].matches("[a-zA-Z]+") || !name[1].matches("[a-zA-Z]+")) {
           return false;
        }
        return true;
    }

    /**
     * For a given player name, performs a search on the homepage and extracts the link to the player's page.
     * If the name is invalid, informs the user.
     * If there isn't such a player, informs the user.
     * @param player - string denoting the players name
     * @return
     *      string denoting the link to the player's page
     */
    public String findPlayerUrl(String player){
        String playerURL = "";

        if(!isNameValid(player)) {
            System.out.println("Invalid input. Input should be in the format: [First name] [Last name]");
        } else {

            try {
                foxDriver.get(startURL);
                driver.waitForAngularRequestsToFinish();

                // search player
                foxDriver.findElement(By.cssSelector(".stats-search__icon")).click();
                foxDriver.findElement(ByAngular.model("search")).sendKeys(player);

                //extract link
                try {
                    WebElement searchPlayer = foxDriver.findElement(By.cssSelector(".stats-search__link-anchor"));

                    playerURL = searchPlayer.getAttribute("href");
                } catch (NoSuchElementException exception) {
                    System.out.println("Player not found.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return playerURL;
         }


    /**
     * Given the player's page, returns the statistics of the 3-point average in the regular season per 40 minutes.
     * If there were no stats found, informs the user.
     * @param pageURL - denotes the link to the player's page
     * @return
     *      collection of (year, 3PA[]) pairs, where a player can have multiple 3PAs per season.
     */
    public HashMap<String,List<String>> extractStats(String pageURL){
        HashMap<String, List<String>> stats = new HashMap<>();

        try{
            foxDriver.get(pageURL);
            driver.waitForAngularRequestsToFinish();

            // filtering
            WebElement filter = foxDriver.findElement(By.name("PerMode"));
            Select selectFilter = new Select(filter);
            selectFilter.selectByVisibleText("Per 40 Minutes");
            driver.waitForAngularRequestsToFinish();

            // data extraction
            try {
                WebElement tableDiv = foxDriver.findElement(By.className("nba-stat-table__overflow"));
                WebElement tableData = tableDiv.findElement(By.tagName("tbody"));
                List<WebElement> rows = tableData.findElements(By.tagName("tr"));

                for(int i=0;i<rows.size();i++){
                    if(!stats.containsKey(rows.get(i).findElement(By.className("first")).getText())){
                        stats.put(rows.get(i).findElement(By.className("first")).getText(),new ArrayList<>());
                    }
                    stats.get(rows.get(i).findElement(By.className("first")).getText()).add(tableData.findElement(By.xpath("//tr["+(i+1)+"]/td[10]")).getText());
                }
            } catch (NoSuchElementException exception){
                exception.printStackTrace();
                System.out.println("Player stats not found.");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return stats;


    }

    /**
     * closes the browser and terminates driver's session
     */
    public void finish(){
        if(foxDriver!=null){
            foxDriver.quit();
        }
    }


}
