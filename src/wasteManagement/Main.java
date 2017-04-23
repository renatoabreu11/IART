package wasteManagement;

import myGraph.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        CityGraph cg = new CityGraph("graph1");

        WasteManagement management = new WasteManagement("station1", cg.getGraph());
        management.printManagementDetails();
        System.out.println();

        Graph graph = cg.getGraph();
        MyGraph myGraph = new MyGraph(graph, management, Waste.GLASS, 0.5, 0.5);

        MyPath path_astar = myGraph.findPath_AStar();
        System.out.println("A*");
        path_astar.printPath();
        System.out.println("\n");

        System.out.println("dfs");
        MyPath path_dfs = myGraph.findPath_dfs();
        path_dfs.printPath();
        System.out.println("\n");

        System.out.println("bfs");
        MyPath path_bfs = myGraph.findPath_bfs();
        path_bfs.printPath();
        System.out.println("\n");

        path_astar.printEdgesOfPath(graph);
        //myGraph.resetColorEdgeOfGraph(graph);
        cg.display();
    }

}
