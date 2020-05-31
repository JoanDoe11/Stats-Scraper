import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestScraper {

    private static final String url = "https://stats.nba.com/";

    @Test
    public void testValidPlayer(){
        try {
            Scraper scraper = new Scraper(url);
            assertEquals("https://stats.nba.com/player/1629029/",scraper.findPlayerUrl("Luka Doncic"));
            scraper.finish();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidPlayer(){
        try {
            Scraper scraper = new Scraper(url);
            assertEquals("",scraper.findPlayerUrl("Luka 132"));
            scraper.finish();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testValidPlayerName(){
        try{
            Scraper scraper = new Scraper(url);
            assertEquals(true, scraper.isNameValid("Luka Doncic"));
            scraper.finish();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidPlayerNameNumbers(){
        try{
            Scraper scraper = new Scraper(url);
            assertEquals(false, scraper.isNameValid("Luka 12D"));
            scraper.finish();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testValidStats(){
        HashMap<String, List<String>> stats = new HashMap<>();
        ArrayList list = new ArrayList();
        list.add("10.9");
        stats.put("2019-20",list);
        list = new ArrayList();
        list.add("8.9");
        stats.put("2018-19",list);
        try{
            Scraper scraper = new Scraper(url);
            assertEquals(stats, scraper.extractStats("https://stats.nba.com/player/1629029/"));
            scraper.finish();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


}
