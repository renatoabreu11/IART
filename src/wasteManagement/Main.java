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

        /*Graph g = CityGraph.generateGraph("graph2", "Dorogovtsev mendes", -1, 30);
        CityGraph.saveGraph(g);*/
        CityGraph cg = new CityGraph("graph2");

        WasteManagement management = new WasteManagement("station2", cg.getGraph());
        management.printManagementDetails();
        System.out.println();

        Graph graph = cg.getGraph();
        MyGraph myGraph = new MyGraph(graph, management, Waste.GLASS, 0.5, 0.5);

        MyPath path_astar = myGraph.findPath_AStar();
        System.out.println("A*");
        path_astar.printPath();
        System.out.println("\n");

        /*System.out.println("dfs");
        MyPath path_dfs = myGraph.findPath_dfs();
        path_dfs.printPath();
        System.out.println("\n");

        System.out.println("bfs");
        MyPath path_bfs = myGraph.findPath_bfs();
        path_bfs.printPath();
        System.out.println("\n");*/

        //path_dfs.printEdgesOfPath(graph, 1);
        //myGraph.resetColorEdgeOfGraph(graph);

        Solver solver = new Solver(myGraph, 7);
        List<MyPath> paths = solver.solve();
        System.out.println("Solver");
        for (int i = 0; i < paths.size(); i++) {
            MyPath path = paths.get(i);
            path.printPath();
            path.printEdgesOfPath(graph, Utils.colors[i]);
        }
        cg.display();
    }

}
