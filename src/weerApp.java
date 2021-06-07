import java.net.URL;
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
            String jsonAntwoord = ht.httpConnect();

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
                System.out.println("Weerdata printen mislukt");
            }
        } catch (Exception e) {
            System.out.println("Weer opvragen mislukt");
        } finally {
            //programma klaar
            System.out.println("***********>>>> Tot zover het weerbericht <<<<**************");
        }
    }
}

//classe om de weer info uit de String te halen en in de terminal te printen
class weerDataPrinter{

    public void getData(String antwoord) throws Exception {

        try {

            //hier gaan we alle data uit de response peuteren

            //plaatsnaam
            String stadRes = antwoord.substring(antwoord.indexOf("name")+7, antwoord.indexOf("\",\"",antwoord.indexOf("name")));

            //weertype
            String weerTypeRes = antwoord.substring(antwoord.indexOf("main")+7, antwoord.indexOf("\",\"",antwoord.indexOf("main")));

            //beschrijving
            String beschrijvingRes = antwoord.substring(antwoord.indexOf("description")+14, antwoord.indexOf("\",\"",antwoord.indexOf("description")));

            //temperatuur in Celsius
            double tempKRes = Double.valueOf(antwoord.substring(antwoord.indexOf("temp")+6, antwoord.indexOf(",\"",antwoord.indexOf("temp"))));
            double tempCRes = tempKRes - 273.15;

            // luchtdruk in hPa
            int luchtdrukRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("pressure")+10, antwoord.indexOf(",\"",antwoord.indexOf("pressure"))));

            // vochtigheid in % - hier check je hoeveel getallen de waarde luchtdruk bevat in de String, met het Character.isDigit commando - dit is extra
            int vochtigheidRes = 0;
            if(Character.isDigit(antwoord.charAt((antwoord.indexOf("humidity")+12)))) {
                vochtigheidRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 13));
            } else if (Character.isDigit(antwoord.charAt((antwoord.indexOf("humidity") + 11)))) {
                vochtigheidRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 12));
            } else {
                vochtigheidRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("humidity") + 10, antwoord.indexOf("humidity") + 11));//
            }

            // windsnelheid in m/s - dit is extra
            double windSpeedRes = Double.valueOf(antwoord.substring(antwoord.indexOf("speed")+7, antwoord.indexOf(",\"deg\"")));

           //windrichting (van graden naar N-Z-O-W) - dit is extra
            int richtingRes = 0;
            if (Character.isDigit(antwoord.charAt((antwoord.indexOf("deg") + 7)))) {
                richtingRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("deg") + 5, antwoord.indexOf("deg") + 8));
            } else if (Character.isDigit(antwoord.charAt((antwoord.indexOf("deg") + 6))))  {
                richtingRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("deg") + 5, antwoord.indexOf("deg") + 7));
            } else {
                richtingRes = Integer.valueOf(antwoord.substring(antwoord.indexOf("deg") + 5, antwoord.indexOf("deg") + 6));
            }

            // van graden naar N O Z W - dit is een extra
            String kompasWaarde = "";
            if ((richtingRes > 337) || (richtingRes < 23)) {
                kompasWaarde = "N"; // 0 graden
            } else if ((richtingRes > 22) && (richtingRes < 68)) {
                kompasWaarde = "NO"; // 45 graden
            } else if ((richtingRes > 67) && (richtingRes < 113)) {
                kompasWaarde = "O"; // 90 graden
            } else if ((richtingRes > 112) && (richtingRes < 158)) {
                kompasWaarde = "ZO"; // 135 graden
            } else if ((richtingRes > 157) && (richtingRes < 203)) {
                kompasWaarde = "Z"; // 180 graden
            } else if ((richtingRes > 202) && (richtingRes < 248)) {
                kompasWaarde = "ZW"; //225 graden
            } else if ((richtingRes > 247) && (richtingRes < 293)) {
                kompasWaarde = "W"; //270 graden
            } else if ((richtingRes > 292) && (richtingRes < 338)) {
                kompasWaarde = "NW"; //315 graden
            }

            // min. temperatuur in Celsius extra
            double tempKminRes = Double.valueOf(antwoord.substring(antwoord.indexOf("temp_min")+10, antwoord.indexOf(",\"",antwoord.indexOf("temp_min"))));
            double tempCminRes = tempKminRes - 273.15;

            // max. temperatuur in Celsius extra
            double tempKmaxRes = Double.valueOf(antwoord.substring(antwoord.indexOf("temp_max")+10, antwoord.indexOf(",\"",antwoord.indexOf("temp_max"))));
            double tempCmaxRes = tempKmaxRes - 273.15;

            System.out.printf("Plaats : " + stadRes + " - weertype : " + weerTypeRes + " - beschrijving : " + beschrijvingRes);
            System.out.printf("\nTemperatuur : %.1f Min. : %.1f Max. : %.1f graden Celsius", tempCRes, tempCminRes, tempCmaxRes);
            System.out.printf("\nLuchtdruk : %4d hPa Luchtvochtigheid : %3d procent", luchtdrukRes, vochtigheidRes);
            System.out.printf("\nWind : %s %.1f m/s\n", kompasWaarde, windSpeedRes);

            //fouten catchen
        } catch (Exception e) {
            System.out.println("Dit ging mis");
            e.printStackTrace();
        }
    }
}

