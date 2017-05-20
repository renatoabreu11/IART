package wasteManagement;

import myGraph.*;
import org.graphstream.algorithm.generator.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        Graph graph = new SingleGraph("graph1.dgs");
        try {
            graph.read("data/graph1.dgs");
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        WasteManagement management = new WasteManagement("station1.xml", graph);
        management.printManagementDetails();

        Solver solver = new Solver(graph, management, Waste.GLASS, 0.5, 0.5, 10);
        solver.solve("A*");
        solver.printInfoAboutSolution();
        List<MyPath> paths = solver.getSolution();

        solver.solve("dfs");
        solver.printInfoAboutSolution();

        solver.solve("bfs");
        solver.printInfoAboutSolution();

        // printing nodes and edges
        MyGraph g = solver.getGraph();
        g.printNodes(graph, g.getNodesWithACertainWaste(Waste.GLASS));

        graph.display();

        for (int i = 0; i < paths.size(); i++) {
            MyPath p = paths.get(i);
            p.printEdgesOfPath(graph, Utils.colors[i]);
            p.printPath();
        }

        g.setWasteProximityFactor();
        for (MyNode node: g.getNodes()) {
            System.out.println("id: " + node.getId() + ", wasteProximity: " + node.getWasteProximity());
        }
    }

    public static Graph generateGraph(String id, String type, int averageDegree, int numNodes){

        Graph g = new SingleGraph(id);
        if(!(type.equals("Random") || type.equals("Barabàsi-Albert") || type.equals("Dorogovtsev mendes"))){
            System.out.println("Invalid type of graph");
            return null;
        }
        Generator gen = null;
        switch(type){
            case "Random":
                gen = new RandomGenerator(averageDegree);
                break;
            case "Barabàsi-Albert":
                gen = new BarabasiAlbertGenerator(averageDegree);
                break;
            case "Dorogovtsev mendes":
                gen = new DorogovtsevMendesGenerator();
                break;
        }

        gen.addSink(g);
        gen.begin();
        for(int i=0; i<numNodes; i++)
            gen.nextEvents();
        gen.end();

        return g;
    }
}
