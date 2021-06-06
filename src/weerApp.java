import java.net.URL;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.SQLOutput;
import java.util.Scanner;

public class weerApp {
    // string van de api key voor openweather
    static String apiKey = "9cbe90aaed00fc55a6bbf47a4b6c2678";

    //default stad als er geen invoer is
    static String city = "Den+Bosch";

    // Main Method die de data via een HTTP client (class weerData) gaat ophalen
    public static void main(String[] args) {

        //In deze string gaat de JSON
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

            //hier maken we een URL object aan met daarin het https adres string (plus de stad en de apiKey strings)
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + stad + "&appid=" + apiKey);


            weerData ht = new weerData(url);
            //hier wordt een string gevuld met een JSON antwoord door de httpConnect functie op te roepen in het aangemaakte weerData object
            String jsonAntwoord = ht.httpConnect("", "GET");

            //het rauwe antwoord
//            System.out.println("nog niks mee gedaan : " + jsonAntwoord);

            //lengte van deze string json
//            System.out.println("lengte JSON : " + json.length());

            //hier wordt het allerlaatste karakter eraf gehaald
            antwoord = jsonAntwoord.substring(0,(jsonAntwoord.length()-1));

            try {
                weerDataPrinter printData = new weerDataPrinter();
                printData.getData(antwoord);
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
class weerDataPrinter{

    public boolean getData(String antwoord) throws Exception {

        try {

            //plaatsnaam
            System.out.println("Stad : " + antwoord.substring(antwoord.indexOf("name")+7, antwoord.indexOf("\",\"",antwoord.indexOf("name"))));

            //weertype
            System.out.println("Weertype : " + antwoord.substring(antwoord.indexOf("main")+7, antwoord.indexOf("\",\"",antwoord.indexOf("main"))));

            //beschrijving
            System.out.println("Beschrijving : " + antwoord.substring(antwoord.indexOf("description")+14, antwoord.indexOf("\",\"",antwoord.indexOf("description"))));

            //temperatuur in Celsius
            double tempF = Double.valueOf(antwoord.substring(antwoord.indexOf("temp")+6, antwoord.indexOf(",\"",antwoord.indexOf("temp"))));
            double tempC = tempF - 273.15;
            System.out.printf("Temperatuur : %.2f", tempC);
            System.out.println(" graden Celsius.");

            // min. temperatuur in Celsius
            double tempFmin = Double.valueOf(antwoord.substring(antwoord.indexOf("temp_min")+10, antwoord.indexOf(",\"",antwoord.indexOf("temp_min"))));
            double tempCmin = tempFmin - 273.15;
            System.out.printf("Min. Temp. : %.2f", tempCmin);
            System.out.println(" graden Celsius.");

            // max. temperatuur in Celsius
            double tempFmax = Double.valueOf(antwoord.substring(antwoord.indexOf("temp_max")+10, antwoord.indexOf(",\"",antwoord.indexOf("temp_max"))));
            double tempCmax = tempFmax - 273.15;
            System.out.printf("Max. Temp. : %.2f", tempCmax);
            System.out.println(" graden Celsius.");

            // luchtdruk in hPa
            int pressure = Integer.valueOf(antwoord.substring(antwoord.indexOf("pressure")+10, antwoord.indexOf(",\"",antwoord.indexOf("pressure"))));
            System.out.println("Luchtdruk: " + pressure + " hPa.");

            // vochtigheid in %
            int humidity = 0;
            if(Character.isDigit(antwoord.charAt((antwoord.indexOf("humidity")+12)))) {
                humidity = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 13));
            } else if (Character.isDigit(antwoord.charAt((antwoord.indexOf("humidity") + 11)))) {
                humidity = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 12));
            } else {
                humidity = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 11));//
            }
            System.out.println("Luchtvochtigheid: " + humidity + " %.");

            // windsnelheid
            double windSpeed = Double.valueOf(antwoord.substring(antwoord.indexOf("speed")+7, antwoord.indexOf(",\"deg\"")));
            System.out.println("Windsnelheid: " + windSpeed + " m/s");

            //windrichting
            int windDeg = Integer.valueOf(antwoord.substring(antwoord.indexOf("deg")+5, antwoord.indexOf(",\"gust\"")));
            String windRichting = "";
            if ((windDeg > 337) || (windDeg < 23)) {
                windRichting = "N"; // 0 graden
            } else if ((windDeg > 22) && (windDeg < 68)) {
                windRichting = "N/O"; // 45 graden
            } else if ((windDeg > 67) && (windDeg < 113)) {
                windRichting = "O"; // 90 graden
            } else if ((windDeg > 112) && (windDeg < 158)) {
                windRichting = "Z/O"; // 135 graden
            } else if ((windDeg > 157) && (windDeg < 203)) {
                windRichting = "Z"; // 180
            } else if ((windDeg > 202) && (windDeg < 248)) {
                windRichting = "Z/W"; //225
            } else if ((windDeg > 247) && (windDeg < 293)) {
                windRichting = "W"; //270
            } else if ((windDeg > 292) && (windDeg < 338)) {
                windRichting = "N/W"; //315
            }
            System.out.println("Windrichting : " + windRichting);

            //fouten catchen
        } catch (Exception e) {
            System.out.println("Dit ging mis");
            e.printStackTrace();
            return false;
        }
        //het is goed gegaan, true returnen
        return true;
    }
}

