package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.w3c.dom.DOMException;
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
    private LineChart<String, Number> chart;

    @FXML
    private DatePicker firstdate;

    @FXML
    private DatePicker seconddate;

    @FXML
    public DatePicker date;

    @FXML
    public TextField charcode;

    @FXML
    public Text text_output;

    @FXML
    void initialize() {

    }

    @FXML
    void onClickMethodDaily(ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParseException {

        LocalDate dateonerequest = date.getValue();
        String strdate = dateonerequest.toString();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd");
        Date date = dt.parse(strdate);
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-mm-yyyy");
        String getUserDate = dt1.format(date).replace('-', '/');

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

    @FXML
    void onClickMethodDynamic(ActionEvent actionEvent) throws IOException, ParseException {
        String getUserCode = charcode.getText();

        LocalDate datefirst = firstdate.getValue();
        String strfdate = datefirst.toString();
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-mm-dd");
        Date datef = dtf.parse(strfdate);
        SimpleDateFormat dt1f = new SimpleDateFormat("dd-mm-yyyy");
        String getUserFDate = dt1f.format(datef).replace('-', '/');

        LocalDate datesecond = seconddate.getValue();
        String strsdate = datesecond.toString();
        SimpleDateFormat dts = new SimpleDateFormat("yyyy-mm-dd");
        Date dates = dts.parse(strsdate);
        SimpleDateFormat dt1s = new SimpleDateFormat("dd-mm-yyyy");
        String getUserSDate = dt1s.format(dates).replace('-', '/');


        String valutecode = Valutecode(getUserCode);

        String url = "http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1="+getUserFDate+"&date_req2="+getUserSDate+"&VAL_NM_RQ="+valutecode;

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine = null;
        StringBuilder listdymamic = new StringBuilder();

        while (true) {
            try {
                if ((inputLine = in.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            listdymamic.append(inputLine);
        }
        in.close();

        NodeList value;
        NodeList date;
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(listdymamic.toString())));

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            XPathExpression valV = xpath.compile("/ValCurs/Record/Value/text()");

            XPathExpression valD = xpath.compile("/ValCurs/Record/@Date");

            value = (NodeList) valV.evaluate(doc, XPathConstants.NODESET);
            date = (NodeList) valD.evaluate(doc, XPathConstants.NODESET);

            //глюк с анимацией
            chart.setAnimated(false);

            series.setName(getUserCode);

            for (int i = 0; i < value.getLength(); i++) {
                String valuestring = value.item(i).getTextContent().replace(',', '.');
                double valued = Double.parseDouble(valuestring);
                series.getData().add(new XYChart.Data<String, Number>(date.item(i).getTextContent(), valued));
            }
            chart.getData().add(series);

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException | DOMException e) {
            e.printStackTrace();
        }

    }

    public static String Valutecode(String getUserCode) throws IOException {
        String url = "http://www.cbr.ru/scripts/XML_daily.asp";

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine = null;
        StringBuilder list = new StringBuilder();

        while (true) {
            try {
                if ((inputLine = in.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            list.append(inputLine);
        }
        in.close();


        NodeList nl;

        String valutecode = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(list.toString())));
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            XPathExpression cod = xpath.compile("//ValCurs/Valute[CharCode='" + getUserCode + "']/@ID");

            nl = (NodeList) cod.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                valutecode = n.getTextContent();
            }

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException | DOMException e) {
            e.printStackTrace();
        }
        return valutecode;
    }
}
