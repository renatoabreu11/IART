package myGraph;


import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import wasteManagement.Waste;

import java.util.*;

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
            MyEdge myEdge = new MyEdge(myFrom, myTo, weight, edge.getIndex());

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
        openList.add(iniState);

        State current = null;
        while (!openList.isEmpty()) {

            current = getLowestF(openList, alfa, beta);
            closedList.add(current);
            openList.remove(current);

            if (current.getIdOfNode() == to.getId())
                return calcPath(iniState, current);

            List<State> adjList = current.getAdjList();
            for (int i = 0; i < adjList.size(); i++) {
                State newState = adjList.get(i);
                if (!openList.contains(newState)) {
                    newState.setParent(current);
                    openList.add(newState);
                }
            }

        }
        return null; // unreachable
    }

    private State getLowestF(List<State> list, double alfa, double beta) {

        State cheapest = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            State node = list.get(i);
            if (node.getF(alfa, beta) < cheapest.getF(alfa, beta))
                cheapest = list.get(i);
        }
        return cheapest;
    }

    public final List<MyNode> findPath_dfs(MyNode from, MyNode to) {

        this.unvisitNodes();
        Stack<MyNode> s = new Stack<MyNode>();
        s.push(from);
        from.setVisited(true);
        while (!s.isEmpty()) {
            MyNode u = s.pop();
            List<MyEdge> adjList = u.getAdjList();
            for (MyEdge v: adjList) {
                if (v.getNodeTo().getVisited())
                    continue;
                v.getNodeTo().setParent(u);
                v.getNodeTo().setVisited(true);
                if (v.getNodeTo().getId() == to.getId()) {
                    return this.calcPath(from, to);
                }
                s.push(v.getNodeTo());
            }
        }

        return null;
    }

    public final List<MyNode> findPath_bfs(MyNode from, MyNode to) {

        this.unvisitNodes();
        Queue<MyNode> q = new LinkedList<MyNode>();
        q.add(from);
        from.setVisited(true);
        while (!q.isEmpty()) {
            MyNode u = q.remove();
            List<MyEdge> adjList = u.getAdjList();
            for (MyEdge v: adjList) {
                if (v.getNodeTo().getVisited())
                    continue;
                v.getNodeTo().setParent(u);
                v.getNodeTo().setVisited(true);
                if (v.getNodeTo().getId() == to.getId()) {
                    return this.calcPath(from, to);
                }
                q.add(v.getNodeTo());
            }
        }

        return null;
    }

    private List<MyNode> calcPath(MyNode start, MyNode goal) {

        LinkedList<MyNode> path = new LinkedList<MyNode>();

        MyNode curr = goal;
        boolean done = false;
        while (!done) {
            path.addFirst(curr);
            curr = curr.getParent();
            if (curr.equals(start))
                done = true;
        }
        return path;
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
        System.out.println("Number of nodes of path: " + path.size());
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
                MyEdge revEdge = new MyEdge(to, from, weight, edge.getIndex());
                revGraph.addEdge(to, revEdge);
            }
        }

        return revGraph;
    }

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

    private void unvisitNodes() {
        for (MyNode node: nodes) {
            node.setVisited(false);
            node.setParent(null);
        }
    }

    public List<MyEdge> getEdgesOfPath(List<MyNode> path) {
        List<MyEdge> edges = new ArrayList<MyEdge>();
        for (int i = 0; i < path.size()-1; i++) {
            MyNode u = path.get(i);
            MyNode v = path.get(i+1);
            List<MyEdge> adjList = u.getAdjList();
            for (MyEdge e: adjList)
                if (e.getNodeTo().getId() == v.getId()) {
                    edges.add(e);
                    break;
                }
        }
        return edges;
    }

    public void printEdgesOfGraph(Graph g, List<MyEdge> edgesOfPath) {
        for (MyEdge e: edgesOfPath)
            g.getEdge(e.getIndex()).addAttribute("ui.color", 1); // 1 = green, 0 = black (see fill-color in stylesheet.css)
    }

    public void resetColorEdgeOfGraph(Graph g) {
        for(Edge e: g.getEachEdge())
            e.addAttribute("ui.color", 0); // 0 = black (see fill-color in stylesheet.css)
    }

}
