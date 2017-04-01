package wasteManagement;

import MyGraph.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.xml.sax.SAXException;
import wasteManagement.CityGraph;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.LinkedList;

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

        management.printManagementDetails();

        Graph graph = cg.getGraph();
        MyGraph myGraph = new MyGraph(graph);
        System.out.println(myGraph);

        Node central = management.getCentral();
        Node wasteStation = management.getWasteStation();

        MyNode central_ = myGraph.getNode(central.getIndex());
        MyNode wasteStation_ = myGraph.getNode(wasteStation.getIndex());
        double truckCap = management.getTrucks().get(0).getMaxCapacity();

        LinkedList<MyNode> path = (LinkedList<MyNode>) myGraph.findPath(central_, wasteStation_, truckCap, Waste.GLASS);
        double glassCollected = 0;
        System.out.println("Path size: " + path.size());
        for (int i = 0; i < path.size(); i++) {
            MyNode node = path.get(i);
            glassCollected += node.getGlass();
            System.out.print(node.getId());
            if (i < path.size()-1) {
                System.out.print(" -> ");
            }
            else {
                System.out.println();
            }
        }
        System.out.println("Glass collected: " + glassCollected);

        cg.display();
    }

}
