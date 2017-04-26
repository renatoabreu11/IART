package myGraph;


import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import wasteManagement.Container;
import wasteManagement.Waste;
import wasteManagement.WasteManagement;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MyGraph {

    private ArrayList<MyNode> nodes = new ArrayList<MyNode>();
    private List<Double> distsWasteStation;

    private double edgesCostSum = 0;
    private double maxCapacityTruck;
    private double alfa;
    private double beta;
    private Waste typeWaste;

    private MyNode from;
    private MyNode to;

    public MyGraph() {}

    public MyGraph(Graph gs, WasteManagement management, Waste typeWaste, double alfa, double beta) {

        this.typeWaste = typeWaste;
        this.importGS(gs, management);
        Node central = management.getCentral();
        Node wasteStation = management.getWasteStation();
        MyNode central_ = this.getNode(central.getIndex());
        MyNode wasteStation_ = this.getNode(wasteStation.getIndex());
        maxCapacityTruck = management.getTrucks().get(0).getMaxCapacity();
        distsWasteStation = this.getDistsToNode(wasteStation_);
        this.alfa = alfa;
        this.beta = beta;
        this.from = central_;
        this.to = wasteStation_;

    }

    public void importGS(Graph graphStream, WasteManagement management) {

        nodes = new ArrayList<MyNode>();

        // get all nodes
        for (int i = 0; i < graphStream.getNodeCount(); i++) {
            Node node = graphStream.getNode(i);
            MyNode newNode = new MyNode(node.getIndex(), 0, 0, 0, 0);
            this.addNode(newNode);
        }

        // add edges to nodes
        for (int i = 0; i < graphStream.getEdgeCount(); i++) {

            Edge edge = graphStream.getEdge(i);
            Node from = edge.getSourceNode();
            Node to = edge.getTargetNode();
            double weight = edge.hasAttribute("weight") ? (double)edge.getAttribute("weight"):0;

            MyNode myFrom = this.getNode(from.getIndex());
            MyNode myTo = this.getNode(to.getIndex());
            MyEdge myEdge = new MyEdge(myFrom, myTo, weight, edge.getIndex());

            this.addEdge(myFrom, myEdge);
            this.edgesCostSum += weight;

            if (!edge.isDirected()) {
                MyEdge myEdge1 = new MyEdge(myTo, myFrom, weight, edge.getIndex());
                this.addEdge(myTo, myEdge1);
                this.edgesCostSum += weight;
            }
        }

        this.readWasteFromManagement(management);

    }

    public void readWasteFromManagement(WasteManagement management) {
        ArrayList<Container> containers = management.getContainers();
        for (Container container : containers) {
            Node node = container.getLocation();
            double household = container.getWasteOfAType(Waste.HOUSEHOLD);
            double plastic = container.getWasteOfAType(Waste.PLASTIC);
            double glass = container.getWasteOfAType(Waste.GLASS);
            double paper = container.getWasteOfAType(Waste.PAPER);
            MyNode myNode = this.getNode(node.getIndex());
            myNode.setWaste(paper, plastic, glass, household);
        }
    }

    public double getMaxCapacityTruck() {return maxCapacityTruck;}

    public MyNode getTo() {return to;}

    public MyNode getFrom() {return from;}

    public MyNode getNode(int id) {
        for (MyNode node: nodes)
            if (node.getId() == id)
                return node;
        return null;
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

    public final MyPath findPath_AStar() {

        LinkedList<State> openList = new LinkedList<State>();
        LinkedList<State> closedList = new LinkedList<State>();
        double sumWaste = this.getTotalWasteOfAType(typeWaste);

        State iniState = new State(from, 0, edgesCostSum, sumWaste, maxCapacityTruck, maxCapacityTruck, distsWasteStation, typeWaste);
        openList.add(iniState);

        State current = null;
        while (!openList.isEmpty()) {
            current = getLowestF(openList);
            closedList.add(current);
            openList.remove(current);

            if (current.getIdOfNode() == to.getId())
                return calcPath(iniState, current);

            List<State> adjList = current.getAdjList();
            for (int i = 0; i < adjList.size(); i++) {
                State newState = adjList.get(i);
                if (!openList.contains(newState) && !closedList.contains(newState)) {
                    newState.setParent(current);
                    openList.add(newState);
                }
            }

        }
        return null; // unreachable
    }

    private State getLowestF(List<State> list) {

        State cheapest = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            State node = list.get(i);
            if (node.getF(alfa, beta) < cheapest.getF(alfa, beta))
                cheapest = list.get(i);
        }
        return cheapest;
    }

    public final MyPath findPath_dfs() {
        return this.findPath_dfs(from, to);
    }

    public final MyPath findPath_dfs(MyNode fromNode, MyNode toNode) {
        this.unvisitNodes();
        Stack<MyNode> s = new Stack<MyNode>();
        s.push(fromNode);
        fromNode.setVisited(true);
        while (!s.isEmpty()) {
            MyNode u = s.pop();
            List<MyEdge> adjList = u.getAdjList();
            for (MyEdge v: adjList) {
                if (v.getNodeTo().getVisited())
                    continue;
                v.getNodeTo().setParent(u);
                v.getNodeTo().setVisited(true);
                if (v.getNodeTo().getId() == toNode.getId()) {
                    return this.calcPath(fromNode, toNode);
                }
                s.push(v.getNodeTo());
            }
        }

        return null;
    }

    public final MyPath findPath_bfs() {
        return this.findPath_bfs(from, to);
    }

    public final MyPath findPath_bfs(MyNode fromNode, MyNode toNode) {
        this.unvisitNodes();
        Queue<MyNode> q = new LinkedList<MyNode>();
        q.add(fromNode);
        fromNode.setVisited(true);
        while (!q.isEmpty()) {
            MyNode u = q.remove();
            List<MyEdge> adjList = u.getAdjList();
            for (MyEdge v: adjList) {
                if (v.getNodeTo().getVisited())
                    continue;
                v.getNodeTo().setParent(u);
                v.getNodeTo().setVisited(true);
                if (v.getNodeTo().getId() == toNode.getId()) {
                    return this.calcPath(fromNode, toNode);
                }
                q.add(v.getNodeTo());
            }
        }
        return null;
    }

    private MyPath calcPath(MyNode start, MyNode goal) {

        LinkedList<MyNode> path = new LinkedList<MyNode>();

        MyNode curr = goal;
        boolean done = false;
        while (!done) {
            path.addFirst(curr);
            curr = curr.getParent();
            if (curr.equals(start))
                done = true;
        }
        path.addFirst(curr);
        return new MyPath(path, maxCapacityTruck, typeWaste);
    }

    private MyPath calcPath(State start, State goal) {

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
        path.addFirst(curr.getNode());
        return new MyPath(path, maxCapacityTruck, typeWaste);
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

    public List<MyNode> getNodesWithWaste() {
        List<MyNode> nodesWithWaste = new ArrayList<MyNode>();

        for (MyNode node: nodes)
            if (node.getWasteReq(typeWaste) > 0)
                nodesWithWaste.add(node);

        Collections.sort(nodesWithWaste,new Comparator<MyNode>(){
            @Override
            public int compare(final MyNode lhs,MyNode rhs) {
                if (lhs.getWasteReq(typeWaste) >= rhs.getWasteReq(typeWaste))
                    return -1;
                else
                    return 1;
            }
        });

        return nodesWithWaste;

    }

    private void unvisitNodes() {
        for (MyNode node: nodes) {
            node.setVisited(false);
            node.setParent(null);
        }
    }

    public void resetColorEdgeOfGraph(Graph g) {
        for(Edge e: g.getEachEdge())
            e.addAttribute("ui.color", Color.black);
    }

    public Waste getTypeWaste() {
        return typeWaste;
    }

}
