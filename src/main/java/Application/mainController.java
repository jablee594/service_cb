package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
    private ComboBox<String> requesthistory;

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
    void initialize() throws ClassNotFoundException, SQLException {
        ArrayList<String> historyList = new ArrayList<>();

        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:~/IdeaProjects/service_cb/db/ExchangeRateDB");

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select name from service");

        while (resultSet.next()) {
            historyList.add(resultSet.getString("name"));
        }

        requesthistory.getItems().setAll(historyList);
        connection.close();
    }

    @FXML
    void onClickMethodDaily(ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParseException, SQLException, ClassNotFoundException {
        if (date.getValue() != null) {
            LocalDate dateonerequest = date.getValue();
            String strdate = dateonerequest.toString();
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd");
            Date date = dt.parse(strdate);
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-mm-yyyy");
            String getUserDate = dt1.format(date).replace('-', '/');


            String getUserCode = charcode.getText();

            if (!getUserCode.equals("")) {
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
                    text_output.setText("???????? "+ getUserCode +" ???? "+ getUserDate +" ????????????????????: "+ result+"");

                    Class.forName("org.h2.Driver");
                    Connection connectionsql = DriverManager.getConnection("jdbc:h2:~/IdeaProjects/service_cb/db/ExchangeRateDB");

                    Statement statement = connectionsql.createStatement();
                    statement.execute("insert into service(name, date, code, valute) values('???????? "+getUserCode+" ????: "+getUserDate+"','"+getUserDate+"', '"+getUserCode+"', '"+result+"')");

                    ResultSet resultSet = statement.executeQuery("select * from service");
                    connectionsql.close();

                    initialize();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("test");
                alert.setHeaderText(null);
                alert.setContentText("?????????????? ??????!");

                alert.showAndWait();
            }

        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("test");
            alert.setHeaderText(null);
            alert.setContentText("?????????????? ????????!");

            alert.showAndWait();
        }
    }

    @FXML
    void onClickMethodDynamic(ActionEvent actionEvent) throws IOException, ParseException, ClassNotFoundException, ParserConfigurationException, SAXException, XPathExpressionException, SQLException {

        if(firstdate.getValue() != null || seconddate.getValue() != null) {
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

            if (datesecond.isAfter(datefirst)) {

                String getUserCode = charcode.getText();
                if (!getUserCode.equals("")) {

                    ArrayList<String> dateList = new ArrayList<>();
                    ArrayList<String> valueList = new ArrayList<>();

                    String valutecode = Valutecode(getUserCode);

                    String url = "http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=" + getUserFDate + "&date_req2=" + getUserSDate + "&VAL_NM_RQ=" + valutecode;

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

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(new InputSource(new StringReader(listdymamic.toString())));

                    XPathFactory xPathfactory = XPathFactory.newInstance();
                    XPath xpath = xPathfactory.newXPath();

                    XPathExpression valV = xpath.compile("/ValCurs/Record/Value/text()");

                    XPathExpression valD = xpath.compile("/ValCurs/Record/@Date");

                    value = (NodeList) valV.evaluate(doc, XPathConstants.NODESET);
                    date = (NodeList) valD.evaluate(doc, XPathConstants.NODESET);

                    //???????? ?? ??????????????????
                    chart.setAnimated(false);

                    series.setName(getUserCode);

                    for (int i = 0; i < value.getLength(); i++) {
                        String valuestring = value.item(i).getTextContent().replace(',', '.');
                        double valued = Double.parseDouble(valuestring);
                        series.getData().add(new XYChart.Data<String, Number>(date.item(i).getTextContent(), valued));
                        dateList.add(date.item(i).getTextContent());
                        valueList.add(String.valueOf(valued));
                    }
                    chart.getData().add(series);


                    Class.forName("org.h2.Driver");
                    Connection connectionsql = DriverManager.getConnection("jdbc:h2:~/IdeaProjects/service_cb/db/ExchangeRateDB");

                    Statement statement = connectionsql.createStatement();
                    statement.execute("insert into service(name, date, code, valute) values('???????? " + getUserCode + " ?? : " + getUserFDate + " ????: " + getUserSDate + "', '" + dateList + "', '" + getUserCode + "', '" + valueList + "')");

                    connectionsql.close();

                    initialize();

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("test");
                    alert.setHeaderText(null);
                    alert.setContentText("?????????????? ??????!");

                    alert.showAndWait();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("test");
                alert.setHeaderText(null);
                alert.setContentText("?????????????? ???????????????????? ????????!");

                alert.showAndWait();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("test");
            alert.setHeaderText(null);
            alert.setContentText("?????????????? ????????!");

            alert.showAndWait();
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

    @FXML
    public void onClickMethodHistory(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        String historyName = requesthistory.getValue();

        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:~/IdeaProjects/service_cb/db/ExchangeRateDB");

        Statement statement = connection.createStatement();

        ResultSet resultName = statement.executeQuery("select * from service where name = '"+ historyName +"'");

        if (historyName.length() > 23){
            //dynamic
            String resultdate = null;
            String resultvalute = null;
            String resultcode = null;
            while (resultName.next()) {
                resultvalute = resultName.getString("valute").replace("[", "").replace("]","");
                resultdate = resultName.getString("date").replace("[", "").replace("]","");
                resultcode = resultName.getString("code");

            }
            ArrayList<String> valuteList = new ArrayList<String>(Arrays.asList(resultvalute.split(",")));
            ArrayList<String> dateList = new ArrayList<String>(Arrays.asList(resultdate.split(",")));

            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();

            chart.setAnimated(false);

            series.setName(resultcode);

            for (int i = 0; i < valuteList.size(); i++) {
                series.getData().add(new XYChart.Data<String, Number>(dateList.get(i), Double.parseDouble(valuteList.get(i))));
            }
            chart.getData().add(series);

        }else {
            //daily
            while (resultName.next()) {
                text_output.setText("???????? "+resultName.getString("code")+" ???? "+resultName.getString("date")+" ????????????????????: "+resultName.getString("valute")+" ");
            }
        }
        connection.close();

        initialize();
    }

    @FXML
    public void onClickMethodClear(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:~/IdeaProjects/service_cb/db/ExchangeRateDB");

        Statement statement = connection.createStatement();
        statement.execute("delete from service");

        connection.close();

        initialize();
    }
}
