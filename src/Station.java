import org.graphstream.graph.Graph;
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
    private ArrayList<Container> containers;
    private Node location;

    public Station(String filename, Graph graph) throws ParserConfigurationException, IOException, SAXException {
        trucks = new ArrayList<>();
        containers = new ArrayList<>();

        File fXmlFile = new File("data/" + filename + ".xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        String locationId = doc.getDocumentElement().getAttribute("location");
        location = graph.getNode(locationId);

        NodeList trucksList = doc.getElementsByTagName("truck");
        for (int i = 0; i < trucksList.getLength(); i++) {

            org.w3c.dom.Node nNode = trucksList.item(i);
            Element eElement = (Element) nNode;

            String category = eElement.getElementsByTagName("category").item(0).getTextContent();
            int capacity = Integer.parseInt(eElement.getElementsByTagName("capacity").item(0).getTextContent());
            Truck t = new Truck(category, capacity);
            trucks.add(t);
        }

        for (Node node : graph) {
            if(node.hasAttribute("waste")){
                Container container = new Container(node);
                containers.add(container);
            }
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

    public ArrayList<Container> getContainers() {
        return containers;
    }

    public void setContainers(ArrayList<Container> containers) {
        this.containers = containers;
    }

    public void printStationDetails(){
        System.out.println("Station location: Node " + location.getId());

        for(Truck t : trucks){
            t.printTruckDetails();
        }

        for(Container c : containers){
            c.printContainerDetails();
        }
    }
}
