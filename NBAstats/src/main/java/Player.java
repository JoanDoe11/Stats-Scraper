import java.util.HashMap;
import java.util.List;

public class Player {
    private String firstName = "";
    private String lastName = "";
    private String pageURL = "";
    private HashMap<String, List<String>> threePA = new HashMap<>();

    public Player() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {

        firstName = firstName.toLowerCase();
        this.firstName = (""+firstName.charAt(0)).toUpperCase()+firstName.substring(1);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        lastName = lastName.toLowerCase();
        this.lastName = (""+lastName.charAt(0)).toUpperCase()+lastName.substring(1);
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public HashMap<String, List<String>> getThreePA() {
        return threePA;
    }

    public void setThreePA(HashMap<String, List<String>> threePA) {
        this.threePA = threePA;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public void printThreePA(){
        StringBuffer buffer = new StringBuffer();
        for (String year:threePA.keySet()){
            for(String stats:threePA.get(year)){
                buffer.append(stats);
                buffer.append(" ");
            }
            System.out.println(year+" "+buffer.toString());
            buffer.delete(0, buffer.length());
        }
    }
}
