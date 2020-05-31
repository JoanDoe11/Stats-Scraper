
import java.io.IOException;

/**
 * @author Jovana Atanasijevic
 */

public class Main {

    private static final String url = "https://stats.nba.com/";

    public static void main(String[] args) {

        Player player = new Player();

        // check input
        if(args.length!=2) {
            System.out.println("Invalid input. Input should be in the format: [First name] [Last name]");
        } else {
            player.setFirstName(args[0]);
            player.setLastName(args[1]);

            try{
                Scraper scraper = new Scraper(url);

                player.setPageURL(scraper.findPlayerUrl(player.toString()));  //find the link of player's page

                if(player.getPageURL().length()!=0){
                    player.setThreePA(scraper.extractStats(player.getPageURL()));   //extract player's stats

                    player.printThreePA();
                }
                scraper.finish();
            } catch (IOException e){
                e.printStackTrace();
            }

        }

    }
}
