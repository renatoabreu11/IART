import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class PathCosts implements AStar.Costs{
    @Override
    public double heuristic(Node node, Node target) {
        return 0;
    }

    @Override
    public double cost(Node parent, Edge from, Node next) {
        return 0;
    }
}
