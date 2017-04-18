package wasteManagement;

import MyGraph.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        CityGraph cg = new CityGraph("graph1");

        WasteManagement management = null;
        try {
            management = new WasteManagement("station1", cg.getGraph());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        //management.printManagementDetails();

        Graph graph = cg.getGraph();
        MyGraph myGraph = new MyGraph(graph);

        Node central = management.getCentral();
        Node wasteStation = management.getWasteStation();
        MyNode central_ = myGraph.getNode(central.getIndex());
        MyNode wasteStation_ = myGraph.getNode(wasteStation.getIndex());
        double truckCap = management.getTrucks().get(0).getMaxCapacity();
        List<Double> distsToStation = myGraph.getDistsToNode(wasteStation_);
        double alfa = 0.5, beta = 0.5;
        List<MyNode> path = myGraph.findPath_AStar(central_, wasteStation_, truckCap, Waste.GLASS, distsToStation, alfa, beta);
        myGraph.printPath(path);


        cg.display();

    }

}
