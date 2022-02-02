package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

public class mainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private LineChart<?, ?> chart;

    @FXML
    private TextField firstdate;

    @FXML
    private TextField seconddate;

    @FXML
    private URL location;

    @FXML
    public Button button_output;

    @FXML
    public TextField date;

    @FXML
    public TextField charcode;

    @FXML
    public Text text_output;

    @FXML
    void initialize() {

    }

    @FXML
    void onClickMethod(ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        String getUserDate = date.getText();
        String getUserCode = charcode.getText();
        if (!getUserDate.equals("")) {
            String output = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + getUserDate;

            URL url = new URL(output);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine = null;
            StringBuffer content = new StringBuffer();

            while (true) {
                try {
                    if ((inputLine = in.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                content.append(inputLine);
            }
            in.close();


            if (!output.isEmpty()) {
                NodeList nl = null;
                String result = null;

                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(content.toString())));
                XPathFactory xPathfactory = XPathFactory.newInstance();
                XPath xpath = xPathfactory.newXPath();

                XPathExpression cod = xpath.compile("//ValCurs/Valute[CharCode='" + getUserCode + "']/Value/text()");

                nl = (NodeList) cod.evaluate(doc, XPathConstants.NODESET);

                for (int i = 0; i < nl.getLength(); i++) {
                    Node n = nl.item(i);
                    result = n.getTextContent();
                }
                text_output.setText("Курс равен: " + result);


            }
        }
    }
}
