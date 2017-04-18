package MyGraph;


import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import wasteManagement.Waste;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyGraph {

    private ArrayList<MyNode> nodes = new ArrayList<MyNode>();

    private double edgesCostSum = 0;

    public MyGraph() {}

    public MyGraph(Graph g) {
        this.importGS(g);
    }

    public MyNode getNode(int id) {
        return nodes.get(id);
    }

    public double getEdgesCostSum() {return edgesCostSum;}

    public int getNumNodes() {
        return nodes.size();
    }

    public void addNode(MyNode node) {
        nodes.add(node);
    }

    public void addEdge(MyNode nodeFrom, MyEdge edge) {
        int idFrom = nodeFrom.getId();
        nodes.get(idFrom).addEdge(edge);
    }

    public ArrayList<MyNode> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<MyNode> nodes) {
        this.nodes = nodes;
    }

    public void importGS(Graph graph) {

        nodes = new ArrayList<MyNode>();

        // get all nodes
        for (int i = 0; i < graph.getNodeCount(); i++) {
            Node node = graph.getNode(i);
            int id = node.getIndex();

            MyNode newNode = null;
            if (!node.hasAttribute("waste")) {
                newNode = new MyNode(id, 0, 0, 0, 0);
            }
            else {
                Map waste = node.getAttribute("waste");
                if (waste == null)
                    System.out.print("dsfsf\n");
                double paper = waste.containsKey("paper") ? (double)waste.get("paper"):0;
                double plastic = waste.containsKey("plastic") ? (double)waste.get("plastic"):0;
                double glass = waste.containsKey("glass") ? (double)waste.get("glass"):0;
                double household = waste.containsKey("household") ? (double)waste.get("household"):0;
                newNode = new MyNode(id, paper, plastic, glass, household);
            }

            this.addNode(newNode);
        }

        // add edges to nodes
        for (int i = 0; i < graph.getEdgeCount(); i++) {

            Edge edge = graph.getEdge(i);
            Node from = edge.getSourceNode();
            Node to = edge.getTargetNode();
            double weight = edge.hasAttribute("weight") ? (double)edge.getAttribute("weight"):0;

            MyNode myFrom = this.getNode(from.getIndex());
            MyNode myTo = this.getNode(to.getIndex());
            MyEdge myEdge = new MyEdge(myFrom, myTo, weight);

            this.addEdge(myFrom, myEdge);
            this.edgesCostSum += weight;
        }

    }

    public String toString() {
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < nodes.size(); i++) {
            MyNode node = nodes.get(i);

            sb.append("Id: " + node.getId() + " paper: " + node.getPaper() + " plastic: " + node.getPlastic()
                    + " glass: " + node.getGlass() + " household: " + node.getHousehold() + "\n");

            sb.append("adjList -> ");
            ArrayList<MyEdge> adjList = node.getAdjList();
            for (int j = 0; j < adjList.size(); j++) {
                MyEdge edge = adjList.get(j);
                sb.append(edge.getNodeTo().getId() + " (" + edge.getWeight() + ")");
                if (j < adjList.size()-1)
                    sb.append(", ");
                else
                    sb.append("\n");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    public final List<MyNode> findPath_AStar(MyNode from, MyNode to, double spaceTruck, Waste wasteType, List<Double> distsWasteStation, double alfa, double beta) {

        LinkedList<State> openList = new LinkedList<State>();
        LinkedList<State> closedList = new LinkedList<State>();
        double sumWaste = this.getTotalWasteOfAType(wasteType);

        State iniState = new State(from, 0, edgesCostSum, sumWaste, spaceTruck, spaceTruck, distsWasteStation, wasteType);
        openList.add(iniState); // add starting node to open list

        boolean done = false;
        State current = null;
        while (!openList.isEmpty()) {
            current = getLowestF(openList, alfa, beta); // get node with lowest fCosts from openList
            closedList.add(current); // add current node to closed list
            openList.remove(current); // delete current node from open list

            if (current.getIdOfNode() == to.getId()) // found goal
                return calcPath(iniState, current);

            double distAtNow = current.getDistAtNow();
            double emptySpace = current.getEmptySpace();

            // for all adjacent nodes:
            List<State> adjList = current.getAdjList();
            for (int i = 0; i < adjList.size(); i++) {
                State newState = adjList.get(i);

                if (!openList.contains(newState)) { // node is not in openList
                    newState.setParent(current); // set current node as previous for this node
                    openList.add(newState); // add node to openList
                }

            }

        }
        return null; // unreachable
    }

    private State getLowestF(List<State> list, double alfa, double beta) {
        State cheapest = list.get(0);
        System.out.println(cheapest);
        for (int i = 1; i < list.size(); i++) {
            State node = list.get(i);
            System.out.println(node);
            if (node.getF(alfa, beta) < cheapest.getF(alfa, beta)) {
                cheapest = list.get(i);
            }
        }
        return cheapest;
    }

    private List<MyNode> calcPath(State start, State goal) {
        LinkedList<MyNode> path = new LinkedList<MyNode>();

        State curr = goal;
        boolean done = false;
        while (!done) {
            path.addFirst(curr.getNode());
            curr = curr.getParent();

            if (curr.equals(start)) {
                done = true;
            }
        }
        return path;
    }

    public void printPath(List<MyNode> path) {
        double glassCollected = 0;
        System.out.println("\n\n\nPath size: " + path.size());
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
    }

    public List<Double> getDistsToNode(MyNode node) {
        MyGraph revGraph = this.getReversed();
        List<Double> dists = revGraph.dijkstra(node);
        return dists;
    }

    public MyGraph getReversed() {

        MyGraph revGraph = new MyGraph();

        ArrayList<MyNode> nodes = this.getNodes();
        for (MyNode node: nodes) {
            MyNode newNode = new MyNode(node.getId(), node.getPaper(), node.getPlastic(), node.getGlass(), node.getHousehold());
            revGraph.addNode(newNode);
        }

        for (MyNode node: nodes) {
            ArrayList<MyEdge> adjList = node.getAdjList();
            for (MyEdge edge: adjList) {
                MyNode from = revGraph.getNode(edge.getNodeFrom().getId());
                MyNode to = revGraph.getNode(edge.getNodeTo().getId());
                double weight = edge.getWeight();
                MyEdge revEdge = new MyEdge(to, from, weight);
                revGraph.addEdge(to, revEdge);
            }
        }

        return revGraph;
    }

    /**
     * Get the distances of nodes of graph to node start, using dijkstra algorithm - O(n^2) version.
     * @param start
     * @param graph
     * @return array of distances
     */
    public ArrayList<Double> dijkstra (MyNode start) {

        ArrayList<Double> dist = new ArrayList<Double>(this.getNumNodes());
        int [] pred = new int [this.getNumNodes()];  // preceeding node in path
        boolean [] visited = new boolean [this.getNumNodes()]; // all false initially

        for (int i=0; i < this.getNumNodes(); i++)
            dist.add(Double.MAX_VALUE);
        dist.set(start.getId(), 0.0);

        for (int i=0; i < dist.size(); i++) {
            int next = minVertex (dist, visited);
            visited[next] = true;

            List<MyEdge> adjList = this.nodes.get(next).getAdjList();

            for (int j = 0; j < adjList.size(); j++) {
                MyNode v = adjList.get(j).getNodeTo();
                double weight = adjList.get(j).getWeight();
                double d = dist.get(next) + weight;
                if (dist.get(v.getId()) > d) {
                    dist.set(v.getId(), d);
                    pred[v.getId()] = next;
                }
            }
        }
        return dist;
    }

    private static int minVertex (List<Double> dist, boolean [] v) {
        double x = Double.MAX_VALUE;
        int y = -1;   // graph not connected, or no unvisited vertices
        for (int i = 0; i < dist.size(); i++) {
            if (!v[i] && dist.get(i) < x) {
                y = i;
                x = dist.get(i);
            }
        }
        return y;
    }

    public double getTotalWasteOfAType(Waste wasteType) {
        double sum = 0;
        for (MyNode node: nodes)
            sum += node.getWasteReq(wasteType);
        return sum;
    }

}
