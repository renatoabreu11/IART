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

    private ArrayList<MyNode> nodes = new ArrayList<>();
    private List<Double> distsWasteStation;

    private double edgesCostSum = 0;
    private double maxCapacityTruck;
    private double alfa;
    private double beta;
    private Waste typeWaste;

    private MyNode from;
    private MyNode to;

    private MyGraph(){}

    MyGraph(Graph gs, WasteManagement management, Waste typeWaste, double alfa, double beta) {
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
        setWasteProximityFactor();
    }

    private void importGS(Graph graphStream, WasteManagement management) {

        nodes = new ArrayList<>();

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

    void readWasteFromManagement(WasteManagement management) {
        ArrayList<Container> containers = management.getContainers();
        for (Container container : containers) {
            Node node = container.getLocation();
            double household = container.getWasteOfAType(Waste.HOUSEHOLD);
            double plastic = container.getWasteOfAType(Waste.PLASTIC);
            double glass = container.getWasteOfAType(Waste.GLASS);
            double paper = container.getWasteOfAType(Waste.PAPER);
            MyNode myNode = this.getNode(node.getIndex());
            if (myNode != null)
                myNode.setWaste(paper, plastic, glass, household);
        }
    }

    public MyNode getTo() {return to;}

    private MyNode getNode(int id) {
        for (MyNode node: nodes)
            if (node.getId() == id)
                return node;
        return null;
    }

    private int getNumNodes() {
        return nodes.size();
    }

    private void addNode(MyNode node) {
        nodes.add(node);
    }

    private void addEdge(MyNode nodeFrom, MyEdge edge) {
        int idFrom = nodeFrom.getId();
        nodes.get(idFrom).addEdge(edge);
    }

    public ArrayList<MyNode> getNodes() {
        return nodes;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder("");

        for (MyNode node : nodes) {
            sb.append("Id: ").append(node.getId()).append(" paper: ").append(node.getPaper()).append(" plastic: ").append(node.getPlastic()).append(" glass: ").append(node.getGlass()).append(" household: ").append(node.getHousehold()).append("\n");

            sb.append("adjList -> ");
            ArrayList<MyEdge> adjList = node.getAdjList();
            for (int j = 0; j < adjList.size(); j++) {
                MyEdge edge = adjList.get(j);
                sb.append(edge.getNodeTo().getId()).append(" (").append(edge.getWeight()).append(")");
                if (j < adjList.size() - 1)
                    sb.append(", ");
                else
                    sb.append("\n");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    final MyPath findPath_AStar() {
        LinkedList<State> openList = new LinkedList<>();
        LinkedList<State> closedList = new LinkedList<>();
        double sumWaste = this.getTotalWasteOfAType(typeWaste);

        State iniState = new State(from, 0, edgesCostSum, sumWaste, maxCapacityTruck, maxCapacityTruck, distsWasteStation, typeWaste);
        openList.add(iniState);

        int counter = 0;
        int maxAttempts = 3;
        List<State> finalStates = new ArrayList<>();

        State current;
        while (!openList.isEmpty()) {
            current = getLowestF(openList);
            closedList.add(current);
            openList.remove(current);

            if (current.getIdOfNode() == to.getId()) {
                finalStates.add(current);
                counter++;
                if (counter >= maxAttempts)
                    break;
            }

            List<State> adjList = current.getAdjList();
            for (State newState : adjList)
                if (!openList.contains(newState) && !closedList.contains(newState)) {
                    newState.setParent(current);
                    openList.add(newState);
                }

        }

        if (finalStates.size() == 0)
            return null; // unreachable

        List<MyPath> paths = calcPathsFromFinalStates(iniState, finalStates);
        return getBestPath(paths);
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

    final MyPath findPath_dfs() {
        double sumWaste = this.getTotalWasteOfAType(typeWaste);
        State iniState = new State(from, 0, edgesCostSum, sumWaste, maxCapacityTruck, maxCapacityTruck, distsWasteStation, typeWaste);
        int counter = 0;
        int maxAttempts = 5;
        Stack<State> s = new Stack<>();
        List<State> visitedStates = new ArrayList<>();
        List<State> finalStates = new ArrayList<>();

        s.push(iniState);
        visitedStates.add(iniState);
        while (!s.isEmpty()) {
            State current = s.pop();

            if (current.getIdOfNode() == to.getId()) {
                finalStates.add(current);
                counter++;
                if (counter >= maxAttempts)
                    break;
            }

            List<State> adjList = current.getAdjList();
            for (State newState : adjList)
                if (!visitedStates.contains(newState)) {
                    newState.setParent(current);
                    visitedStates.add(newState);
                    s.push(newState);
                }
        }

        if (finalStates.size() == 0)
            return null; // unreachable

        List<MyPath> paths = calcPathsFromFinalStates(iniState, finalStates);
        return getBestPath(paths);
    }

    final MyPath findPath_bfs() {
        double sumWaste = this.getTotalWasteOfAType(typeWaste);
        State iniState = new State(from, 0, edgesCostSum, sumWaste, maxCapacityTruck, maxCapacityTruck, distsWasteStation, typeWaste);
        int counter = 0;
        int maxAttempts = 5;
        Queue<State> s = new ArrayDeque<>();
        List<State> visitedStates = new ArrayList<>();
        List<State> finalStates = new ArrayList<>();

        s.add(iniState);
        visitedStates.add(iniState);
        while (!s.isEmpty()) {
            State current = s.remove();

            if (current.getIdOfNode() == to.getId()) {
                finalStates.add(current);
                counter++;
                if (counter >= maxAttempts)
                    break;
            }

            List<State> adjList = current.getAdjList();
            for (State newState : adjList)
                if (!visitedStates.contains(newState)) {
                    newState.setParent(current);
                    visitedStates.add(newState);
                    s.add(newState);
                }
        }

        if (finalStates.size() == 0)
            return null; // unreachable

        List<MyPath> paths = calcPathsFromFinalStates(iniState, finalStates);
        return getBestPath(paths);
    }

    private List<MyPath> calcPathsFromFinalStates(State iniState, List<State> finalStates) {
        List<MyPath> paths = new ArrayList<>();
        for (State state: finalStates)
            paths.add(calcPath(iniState, state));
        return paths;
    }

    private MyPath getBestPath(List<MyPath> paths) {
        int indexBestPath = 0;
        double maxProfit = -1;
        for (int i = 0; i < paths.size(); i++) {
            MyPath path = paths.get(i);
            double profit = path.getSumWasteCollected()/path.getDistPercorred();
            if (profit > maxProfit) {
                maxProfit = profit;
                indexBestPath = i;
            }
        }
        return paths.get(indexBestPath);
    }

    private MyPath calcPath(State start, State goal) {

        LinkedList<MyNode> path = new LinkedList<>();

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

    private List<Double> getDistsToNode(MyNode node) {
        MyGraph revGraph = this.getReversed();
        return revGraph.dijkstra(node);
    }

    private MyGraph getReversed() {
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

        revGraph.beta = beta;
        revGraph.alfa = alfa;
        revGraph.typeWaste = typeWaste;

        return revGraph;
    }

    private ArrayList<Double> dijkstra(MyNode start) {
        ArrayList<Double> dist = new ArrayList<>(this.getNumNodes());
        boolean [] visited = new boolean [this.getNumNodes()]; // all false initially

        for (int i=0; i < this.getNumNodes(); i++)
            dist.add(Double.MAX_VALUE);
        dist.set(start.getId(), 0.0);

        for (int i=0; i < dist.size(); i++) {
            int next = minVertex (dist, visited);
            visited[next] = true;

            List<MyEdge> adjList = this.nodes.get(next).getAdjList();

            for (MyEdge anAdjList : adjList) {
                MyNode v = anAdjList.getNodeTo();
                double weight = anAdjList.getWeight();
                double d = dist.get(next) + weight;
                if (dist.get(v.getId()) > d) {
                    dist.set(v.getId(), d);
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

    private double getTotalWasteOfAType(Waste wasteType) {
        double sum = 0;
        for (MyNode node: nodes)
            sum += node.getWasteReq(wasteType);
        return sum;
    }

    boolean isNecessaryCollectWaste(double containerCap) {
        for (MyNode node: nodes)
            if (node.getWasteReq(typeWaste) >= containerCap / 2)
                return true;
        return false;
    }

    public List<MyNode> getNodesWithACertainWaste(Waste wasteType) {
        List<MyNode> nodesWithWaste = new ArrayList<>();

        for (MyNode node: nodes)
            if (node.getWasteReq(wasteType) > 0)
                nodesWithWaste.add(node);

        return nodesWithWaste;
    }

    private void unvisitNodes() {
        for (MyNode node: nodes) {
            node.setVisited(false);
            node.setParent(null);
        }
    }

    /**
     * To each node with waste: run bfs (in reversed graph)
     * to calculate the waste proximity factor.
     */
    public void setWasteProximityFactor() {
        MyGraph reversed = getReversed();

        List<MyNode> nodesWithWaste = reversed.getNodesWithACertainWaste(typeWaste);
        for (MyNode node: nodesWithWaste)
            reversed.setWasteProximityFactorToANodeWithWaste(node);

        for (MyNode node: reversed.getNodes()) {
            int idNode = node.getId();
            double wasteProximity = node.getWasteProximity();
            MyNode nodeAux = getNode(idNode);
            if (nodeAux != null)
                nodeAux.setWasteProximity(wasteProximity);
        }
    }

    private void setWasteProximityFactorToANodeWithWaste(MyNode node) {
        unvisitNodes();

        Queue<Pair<MyNode, Double>> q = new LinkedList<>();
        Pair<MyNode, Double> initPair = Pair.of(node, node.getWasteReq(typeWaste));
        q.add(initPair);
        node.setVisited(true);

        while (!q.isEmpty()) {
            Pair<MyNode, Double> currPair = q.remove();
            MyNode nodeFrom = currPair.first;
            double wasteProximity = currPair.second;
            nodeFrom.setWasteProximity(wasteProximity + nodeFrom.getWasteProximity());

            List<MyEdge> adjList = nodeFrom.getAdjList();
            for (MyEdge v: adjList) {
                MyNode nodeTo = v.getNodeTo();
                if (nodeTo.getVisited())
                    continue;

                nodeTo.setVisited(true);
                q.add(Pair.of(nodeTo, wasteProximity * 0.5));
            }
        }
    }

    public void resetColorEdgeOfGraph(Graph g) {
        for(Edge e: g.getEachEdge())
            e.addAttribute("ui.color", Color.black);
    }

    public void printNodes(Graph g, List<MyNode> nodesToPrint) {
        for (MyNode n: nodesToPrint) {
            Node n_= g.getNode(n.getId());
            n_.setAttribute("ui.class", "marked");
        }
    }
}
