import java.io.InputStream;
import java.io.StringWriter;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;

//classe om een URL te parsen - voorbeeld naar http://javaonlinelessons.com/misc/how-to-get-weather-data-using-java/
public final class weerData {

    //URL classe is final (uit java.net.*)
    private URL httpurl = null;

    public weerData(URL httpurl_) {
        httpurl = httpurl_;
    }

    public String httpConnect(String outStr, String connectMethod) throws Exception {

        //geef aan dat je in de methode zit
        System.out.println("Starting HTTP Request....");

        //Start een nieuwe HttpURLConnection (uit java.net.*) waarbij je het URL object cast als een HttpURLConnection en de openConnection methode gebruikt van het URL object
        HttpURLConnection verbinding = (HttpURLConnection) httpurl.openConnection();

        //Connectie krijgen met de connect() methode uit het HttpURLConnection object 'verbinding'
        verbinding.connect();

        // Antwoord van de connectie krijgen met de getResponseCode() methode
        System.out.println("Connection Response code:" + verbinding.getResponseCode());


        //Uitlezen HTTP antwoord.
        StringWriter antwoordStringWriter = new StringWriter();
        InputStream antwoord = null;

        try {
            //gebruik de methode getInputStream die in het HttpURLConnection object 'verbinding' zit om de leash 'antwoord' van InputStream te koppelen. Een Inputstream is een stroom informatie die vloeit uit een open connectie (HTTP)
            antwoord = verbinding.getInputStream();

            // je gebruikt nu de read() methode uit de InputStream 'antwoord' om een integer te vullen met de data die binnenkomt. De getallen in de integers staan gelijk aan ASCII codes voor tekens
            int leesGetal = antwoord.read();

            System.out.println("check this out : " + leesGetal);

            //en iedere integer die binnenkomt gaan we wegschrijven in het StringWriter object 'antwoordStringWriter'.
            // Een Stringwriter slaat een characterstroom op in een string buffer, die vervolgens gebruikt kan worden om een string te bouwen
            // als je de write() methode gebruikt met een integer bijv. write(123) dan wordt een character met ASCII characternummer 123 weggeschreven (dat is "{" voor 123)
            antwoordStringWriter.write(leesGetal);

            //zolang er een getal groter dan nul uit de inputstream in de integer 'rint' gezet kan worden, blijf hetvolgende doen
            while (leesGetal > 0) {
                //hier laat je het getal dat binnenkomt uit de printen
//                System.out.println("het getal : " + rint);

                //hier laat je de inputstream met methode read() het character-integer uitlezen en in de integer rint zetten
                leesGetal = antwoord.read();

                // het lees getal uit en schrijf als character weg in de antwoordStringWriter
                antwoordStringWriter.write(leesGetal);
            }
            //vang fouten (exceptions) af
        } catch (Exception ex) {
            //print de responsecode, als die niet 200 is (= alles okay), dan een IOException throwen
            System.out.println("HTTP ResponseCode=" + verbinding.getResponseCode() + " plaats niet gevonden");
            if (verbinding.getResponseCode() != 200) {
                throw new IOException(verbinding.getResponseMessage());
            }
        }

        //Http antwoord uitprinten uit antwoordStringWriter
//        System.out.println(antwoordStringWriter.toString());

        //return een String door de antwoordStringWriter met de methode toString() om te zetten tot String
        return antwoordStringWriter.toString();
    }
}

