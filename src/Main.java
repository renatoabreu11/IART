import MyGraph.*;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

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
        String myGraphStr = myGraph.toString();
        System.out.print(myGraphStr);

        cg.display();

    }

}
