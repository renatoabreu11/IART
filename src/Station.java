import org.graphstream.graph.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Station {
    private ArrayList<Truck> trucks;
    private Node location;

    public Station(String filename) throws ParserConfigurationException, IOException, SAXException {
        trucks = new ArrayList<>();

        File fXmlFile = new File("data/" + filename + ".xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        String locationId = doc.getDocumentElement().getAttribute("location");

        NodeList trucksList = doc.getElementsByTagName("truck");
        for (int i = 0; i < trucksList.getLength(); i++) {

            org.w3c.dom.Node nNode = trucksList.item(i);
            Element eElement = (Element) nNode;

            String category = eElement.getElementsByTagName("category").item(0).getTextContent();
            int capacity = Integer.parseInt(eElement.getElementsByTagName("capacity").item(0).getTextContent());
            Truck t = new Truck(category, capacity);
            trucks.add(t);
        }
    }

    public ArrayList<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(ArrayList<Truck> trucks) {
        this.trucks = trucks;
    }


    public Node getLocation() {
        return location;
    }

    public void setLocation(Node location) {
        this.location = location;
    }
}
