package Parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;

public class parser {
    public static String parser(String codeval, StringBuffer list) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

        NodeList nl = null;
        String result = null;

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(list.toString())));
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        XPathExpression cod = xpath.compile("//ValCurs/Valute[CharCode='" + codeval +"']/Value/text()");

        nl = (NodeList) cod.evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            result = "Value:" + n.getTextContent();
            //System.out.println("Value:" + n.getTextContent());
        }
        System.out.print(result);

        return result;
    }
}
