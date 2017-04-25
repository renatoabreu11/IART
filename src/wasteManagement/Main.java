package wasteManagement;

import myGraph.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        CityGraph cg = new CityGraph("graph2");
        WasteManagement management = new WasteManagement("station2", cg.getGraph());
        management.printManagementDetails();
        Graph graph = cg.getGraph();

        // IMP: each solver needs a new instance of graph
        MyGraph myGraph = new MyGraph(graph, management, Waste.GLASS, 0.1, 0.9);
        Solver solver = new Solver(myGraph, 2);
        solver.solve("A*");
        solver.printInfoAboutSolution();
        List<MyPath> paths = solver.getSolution();
        for (MyPath path: paths)
            path.printPath();

        myGraph = new MyGraph(graph, management, Waste.GLASS, 0.5, 0.5);
        solver = new Solver(myGraph, 1);
        solver.solve("dfs");
        solver.printInfoAboutSolution();

        myGraph = new MyGraph(graph, management, Waste.GLASS, 0.5, 0.5);
        solver = new Solver(myGraph, 1);
        solver.solve("bfs");
        solver.printInfoAboutSolution();

        cg.display();
    }

}
