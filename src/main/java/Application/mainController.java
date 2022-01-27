package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class mainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    public TextField date;

    @FXML
    public TextField charcode;

    @FXML
    void initialize() {

    }

    public void onClickMethod(ActionEvent actionEvent) throws IOException {
        String getUserDate = date.getText();
        if (!getUserDate.equals("")) {
            String output = getUrlContent("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + date);

            if (!output.isEmpty()){

            }
        }
    }

    private static String getUrlContent(String urlAddress) throws IOException {

        URL url = new URL(urlAddress);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine = null;
        StringBuffer content = new StringBuffer();

        while (true) {
            try {
                if ((inputLine = in.readLine()) == null) break;
            } catch (IOException e) {
                System.out.println("Неверно введенная дата");
            }
            content.append(inputLine);
        }
        in.close();

        System.out.println(content);

        return content.toString();
    }
}
