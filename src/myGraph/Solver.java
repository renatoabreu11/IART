package myGraph;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    private int numTrucks;
    private MyGraph graph;


    public Solver(MyGraph graph, int numTrucks) {
        this.graph = graph;
        this.numTrucks = numTrucks;
    }

    public List<MyPath> solve() {

        List<MyPath> paths = new ArrayList<MyPath>();
        for (int i = 0; i < numTrucks; i++) {
            MyPath path = graph.findPath_AStar();
            paths.add(path);
            path.updateWasteInNodes();
            if (graph.getTotalWasteOfAType(graph.getTypeWaste()) <= 0) {
                break;
            }
        }

        return paths;
    }

}
