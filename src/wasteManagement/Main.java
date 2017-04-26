package wasteManagement;

import javafx.scene.paint.Color;
import myGraph.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        CityGraph cg = new CityGraph("graph1.dgs");
        WasteManagement management = new WasteManagement("station1.xml", cg.getGraph());
        management.printManagementDetails();
        Graph graph = cg.getGraph();

        Solver solver = new Solver(graph, management, Waste.GLASS, 0.2, 0.8, 2);
        solver.solve("A*");
        solver.printInfoAboutSolution();
        List<MyPath> paths = solver.getSolution();
        paths.get(0).printEdgesOfPath(graph, java.awt.Color.blue);
        paths.get(0).printPath();

        solver.solve("dfs");
        solver.printInfoAboutSolution();

        solver.solve("bfs");
        solver.printInfoAboutSolution();

        cg.display();
    }

}
