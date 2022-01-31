package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private URL location;

    @FXML
    public TextField date;

    @FXML
    public TextField charcode;

    @FXML
    public Text text_output;

    @FXML
    void initialize() {

    }

    public void onClickMethod(ActionEvent actionEvent) throws IOException, ParserConfigurationException, XPathExpressionException, SAXException {
        String getUserDate = date.getText();
        if (!getUserDate.equals("")) {
            String output = getUrlContent("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + date);
            StringBuffer content = new StringBuffer();

            URL url = new URL(urlAddress);
            URLConnection urlConn = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;


            String result = null;
            if (!output.isEmpty()) {
                NodeList nl = null;
                result = null;

                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(content.toString())));
                XPathFactory xPathfactory = XPathFactory.newInstance();
                XPath xpath = xPathfactory.newXPath();

                XPathExpression cod = xpath.compile("//ValCurs/Valute[CharCode='" + charcode + "']/Value/text()");

                nl = (NodeList) cod.evaluate(doc, XPathConstants.NODESET);

                for (int i = 0; i < nl.getLength(); i++) {
                    Node n = nl.item(i);
                    result = n.getTextContent();
                }

                text_output.setText("Курс равен: " + result);


            }
        }
    }
