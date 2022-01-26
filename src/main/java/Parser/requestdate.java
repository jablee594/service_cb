package Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class requestdate {
    String date = "22/01/2001";
    String codeval = "22/01/2001";

    public StringBuffer request(String[] args) throws IOException {

        String urlcb = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + date;
        URL url = new URL(urlcb);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine = null;
        StringBuffer list = new StringBuffer();

        while (true) {
            try {
                if ((inputLine = in.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            list.append(inputLine);
        }
        in.close();
        return list;
    }
}
