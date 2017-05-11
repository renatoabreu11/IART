package wasteManagement;

import myGraph.*;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        CityGraph cg = new CityGraph("gridGraph.dgs");
        WasteManagement management = new WasteManagement("station4.xml", cg.getGraph());
        management.printManagementDetails();
        Graph graph = cg.getGraph();

        Solver solver = new Solver(graph, management, Waste.GLASS, 0.1, 0.9, 10);
        solver.solve("A*");
        solver.printInfoAboutSolution();
        List<MyPath> paths = solver.getSolution();

        solver.solve("dfs");
        solver.printInfoAboutSolution();

        solver.solve("bfs");
        solver.printInfoAboutSolution();

        // printing nodes and edges
        MyGraph g = solver.getGraph();
        g.printNodes(cg.getGraph(), g.getNodesWithACertainWaste(Waste.GLASS));

        cg.display();

        for (int i = 0; i < paths.size(); i++) {
            MyPath p = paths.get(i);
            p.printEdgesOfPath(graph, Utils.colors[i]);
            p.printPath();
            sleep();
        }

        g.setWasteProximityFactor();
        for (MyNode node: g.getNodes()) {
            System.out.println("id: " + node.getId() + ", wasteProximity: " + node.getWasteProximity());
        }

    }

    protected static void sleep() {
        try { Thread.sleep(2000); } catch (Exception e) {}
    }

}
