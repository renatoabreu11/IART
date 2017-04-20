package wasteManagement;

import MyGraph.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.xml.sax.SAXException;

import javax.security.sasl.SaslServer;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        CityGraph cg = new CityGraph("graph1");

        WasteManagement management = new WasteManagement("station1", cg.getGraph());
        management.printManagementDetails();
        System.out.println();

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
        System.out.println("A*");
        myGraph.printPath(path);
        System.out.println("\n");

        System.out.println("dfs");
        path = myGraph.findPath_dfs(central_, wasteStation_);
        myGraph.printPath(path);
        System.out.println("\n");

        System.out.println("bfs");
        path = myGraph.findPath_bfs(central_, wasteStation_);
        myGraph.printPath(path);
        System.out.println("\n");

        cg.display();
    }

}
