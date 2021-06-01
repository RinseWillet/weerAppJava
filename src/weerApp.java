import java.net.URL;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.Scanner;

public class weerApp {

    static String apiKey = "9cbe90aaed00fc55a6bbf47a4b6c2678";
    static String city = "Den+Bosch";

    // Main Method die de data via een HTTP client (class weerData) gaat ophalen
    public static void main(String[] args) {
        String antwoord = "";

        String city = "Den+Bosch";
        System.out.println("Voer je plaats in: ");
        Scanner invoer = new Scanner(System.in);
        String input = invoer.nextLine();
        if (input.isEmpty()){
            input = city;
        }
        String stad = input.replaceAll(" ", "+").toLowerCase();

        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + stad + "&appid=" + apiKey);
            weerData ht = new weerData(url);
            //Connect to a HTTP url with "Connect Method", "User" and "Password" if required only.
            String json = ht.httpConnect("", "GET", "user", "password");
//            System.out.println("lengte JSON : " + json.length());
            antwoord = json.substring(0,(json.length()-1));
            try {
                weerDataParser parseData = new weerDataParser();
                parseData.getData(antwoord);
            } catch (Exception e) {
                System.out.println("werkt niet");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //programma klaar
        System.out.println("***********>>>> Tot zover het weerbericht <<<<**************");
    }

}

//classe om de weer info uit de String te halen en in de terminal te printen
class weerDataParser{

    public boolean getData(String antwoord) throws Exception {

        //JSON parser object aanmaken
        JSONParser parser = new JSONParser();

        try {

            //met de parser het argument (antwoord) als string gaan parsen naar een JSONObject
            JSONObject jsonObject = (JSONObject) parser.parse(antwoord);

            //Welke stad
            System.out.println("Stad : " + jsonObject.get("name"));

            //algemene array uit object halen en printen
            JSONArray tempData = (JSONArray) jsonObject.get("weather");
            System.out.println("Weertype: " + ((JSONObject) tempData.get(0)).get("main"));
            System.out.println("Beschrijving: " + ((JSONObject) tempData.get(0)).get("description"));

            //details data uit object halen, in array zetten en vervolgens printen
            JSONObject tempData1 = (JSONObject) jsonObject.get("main");
            System.out.printf("Temperatuur: %.2f",  (Double.parseDouble(tempData1.get("temp").toString()) - 273.15)); //in Deg C
            System.out.println(" graden Celsius.");
            System.out.printf("Temperatuur Min: %.2f",(Double.parseDouble(tempData1.get("temp_min").toString()) - 273.15));//in Deg C
            System.out.println(" graden Celsius.");
            System.out.printf("Temperatuur Max: %.2f", (Double.parseDouble(tempData1.get("temp_max").toString()) - 273.15));//in Deg C
            System.out.println(" graden Celsius.");
            System.out.println("Luchtdruk: " + tempData1.get("pressure") + " hPa."); //hpa
            System.out.println("Luchtvochtigheid: " + tempData1.get("humidity") + " %."); //%

            //fouten catchen
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //het is goed gegaan, true returnen
        return true;
    }
}

