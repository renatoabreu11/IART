package wasteManagement;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import wasteManagement.Container;
import wasteManagement.Truck;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

public class WasteManagement {

    private ArrayList<Truck> trucks;
    private ArrayList<Container> containers;
    private ArrayList<Map.Entry<Waste, Double>> residueBuildup;
    private Node central;
    private Node wasteStation;
    private double distCentralStation;

    public WasteManagement(String filename, Graph graph) throws IOException, SAXException, ParserConfigurationException {
        trucks = new ArrayList<>();
        containers = new ArrayList<>();

        residueBuildup = new ArrayList<>();
        residueBuildup.add(new AbstractMap.SimpleEntry<>(Waste.HOUSEHOLD, 0.0));
        residueBuildup.add(new AbstractMap.SimpleEntry<>(Waste.PAPER, 0.0));
        residueBuildup.add(new AbstractMap.SimpleEntry<>(Waste.PLASTIC, 0.0));
        residueBuildup.add(new AbstractMap.SimpleEntry<>(Waste.GLASS, 0.0));

        parseDocument("data/" + filename + ".xml", graph);
    }

    private void parseDocument(String s, Graph graph) throws ParserConfigurationException, IOException, SAXException {
        File fXmlFile = new File(s);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        String centralId = doc.getElementsByTagName("central").item(0).getTextContent();
        central = graph.getNode(centralId);

        String stationId = doc.getElementsByTagName("wasteStation").item(0).getTextContent();
        wasteStation = graph.getNode(stationId);

        AStar astar = new AStar(graph);
        astar.setCosts(new AStar.DefaultCosts());
        astar.compute(centralId, stationId);
        Path path = astar.getShortestPath();

        distCentralStation = path.getPathWeight("weight");

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
                ArrayList<Map.Entry<Waste, Double>> residues = container.getResidues();
                addResiduesToBuildup(residues);
                containers.add(container);
            }
        }
    }

    private void addResiduesToBuildup(ArrayList<Map.Entry<Waste, Double>> residues) {
        for(int i = 0; i < residues.size(); i++){
            Map.Entry<Waste, Double> entry = residues.get(i);
            Waste key = entry.getKey();
            double value = entry.getValue();
            int index = -1;
            switch (key){
                case HOUSEHOLD:
                    index = 0;
                    break;
                case PAPER:
                    index = 1;
                    break;
                case PLASTIC:
                    index = 2;
                    break;
                case GLASS:
                    index = 3;
                    break;
            }
            double currValue = residueBuildup.get(index).getValue();
            residueBuildup.get(index).setValue(currValue + value);
        }
    }

    public ArrayList<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(ArrayList<Truck> trucks) {
        this.trucks = trucks;
    }

    public ArrayList<Container> getContainers() {
        return containers;
    }

    public void setContainers(ArrayList<Container> containers) {
        this.containers = containers;
    }

    public void printManagementDetails(){
        System.out.println("Central location: MyNode " + central.getId());
        System.out.println("wasteManagement.Truck.Waste station location: MyNode " + wasteStation.getId());
        System.out.println("Distance between central and waste station: " + distCentralStation);

        System.out.println("\nCompany trucks");
        for(Truck t : trucks){
            t.printTruckDetails();
        }

        System.out.println("\nCompany containers");
        for(Container c : containers){
            c.printContainerDetails();
        }

        System.out.println("Residue buildup");
        for(int i = 0; i < residueBuildup.size(); i++) {
            Map.Entry<Waste, Double> entry = residueBuildup.get(i);
            Waste key = entry.getKey();
            double value = entry.getValue();
            System.out.println(key + ": " + value + "kg.");
        }
    }

    public double getDistCentralStation() {
        return distCentralStation;
    }

    public void setDistCentralStation(double distCentralStation) {
        this.distCentralStation = distCentralStation;
    }

    public ArrayList<Map.Entry<Waste, Double>> getResidueBuildup(){
       return residueBuildup;
    }

    public void setResidueBuildup(ArrayList<Map.Entry<Waste, Double>> residueBuildup) {
        this.residueBuildup = residueBuildup;
    }

    public Node getCentral() {
        return central;
    }

    public Node getWasteStation() {
        return wasteStation;
    }
}
