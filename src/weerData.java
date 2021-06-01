import java.io.*;
import java.net.*;
import java.io.StringWriter;
import java.util.Base64;
import java.util.Base64.*;
import javax.net.ssl.*;

//classe om een URL te parsen - voorbeeld naar http://javaonlinelessons.com/misc/how-to-get-weather-data-using-java/
public final class weerData {

    private String auth = null;
    private URL httpurl = null;

    public weerData (URL httpurl_) {
        httpurl = httpurl_;
    }

    public String httpConnect(String outStr, String connectMethod, String userNm,
                              String pass) throws Exception {

        System.out.println("Starting HTTP Request....");
        //Start een nieuwe HttpURLConnection
        HttpURLConnection con = (HttpURLConnection) httpurl.openConnection();

        // Authorizatie krijgen
        if (auth != null) {
            con.setRequestProperty("Authorization", "Basic " + auth);
        }

        if (userNm == null) {
            auth = null;
        }
        auth = userNm + ":" + ((pass == null) ? "" : pass);
        Encoder encoder = Base64.getEncoder();
        auth = encoder.encodeToString(auth.getBytes());

        //Anonieme verbinding zonder certificaten
        if (con instanceof HttpsURLConnection) {
            HttpsURLConnection https_con = (HttpsURLConnection) con;
            //Create dummy Hostname Verifier
            https_con.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String nm, SSLSession session) {
                    return true;
                }
            });
        }

        //connectie parameters instellen
        con.setInstanceFollowRedirects(false);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setAllowUserInteraction(true);
        con.setRequestMethod(connectMethod.toUpperCase());

        //Connectie antwoord krijgen
        if (connectMethod.equals(connectMethod)) {
            con.connect();
            System.out.println("Connection Response code:" + con.getResponseCode());
        }

        if (connectMethod.equals("POST") || connectMethod.equals("PUT")) {
            OutputStream out = con.getOutputStream();
            out.write(outStr.getBytes(), 0, outStr.getBytes().length);
            out.flush();

            try {
                out.close();
            } catch (Exception e) {
            }
        }

        //Uitlezen HTTP antwoord.
        StringWriter strWtr = new StringWriter();
        InputStream instr = null;

        try {
            instr = con.getInputStream();
            int rint = instr.read();
            strWtr.write(rint);
            while (rint > 0) {
                rint = instr.read();
                strWtr.write(rint);
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            System.out.println("HTTP ResponseCode=" + con.getResponseCode());

            if ((con.getResponseCode() / 100) != 2) {
                throw new IOException(con.getResponseMessage());
            }

        }

        //Http antwoord uitprinten
        System.out.println(strWtr.toString());

        try {
            instr.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return strWtr.toString();
    }

    // Dummy Trust Manager for secured connection, it does
    //not validate certificate chains
    static {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sc.getSocketFactory();

            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
        } catch (Exception e) {
            System.out.println("Error in trust manager...");
            e.printStackTrace();
        }
    }
}

