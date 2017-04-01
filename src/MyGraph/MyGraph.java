package MyGraph;


import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import wasteManagement.Waste;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyGraph {

    private ArrayList<MyNode> nodes = new ArrayList<MyNode>();

    public MyGraph(Graph g) {
        this.importGS(g);
    }

    public MyNode getNode(int id) {
        return nodes.get(id);
    }

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
        }

    }

    public String toString() {
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < nodes.size(); i++) {
            MyNode node = nodes.get(i);

            sb.append(node.getId() + " paper: " + node.getPaper() + " plastic: " + node.getPlastic()
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
        }

        return sb.toString();
    }

    public final List<MyNode> findPath(MyNode from, MyNode to, double spaceTruck, Waste wasteType) {

        from.setState(0, spaceTruck);

        LinkedList<MyNode> openList = new LinkedList<MyNode>();
        LinkedList<MyNode> closedList = new LinkedList<MyNode>();
        openList.add(from); // add starting node to open list

        boolean done = false;
        MyNode current = null;
        while (!done) {
            current = getLowestF(openList, spaceTruck); // get node with lowest fCosts from openList
            closedList.add(current); // add current node to closed list
            openList.remove(current); // delete current node from open list

            if (current.getId() == to.getId()) { // found goal
                return calcPath(from, to);
            }

            double distAtNow = current.getDistAtNow();
            double emptySpace = current.getEmptySpace();

            // for all adjacent nodes:
            ArrayList<MyEdge> adjList = current.getAdjList();
            for (int i = 0; i < adjList.size(); i++) {

                MyEdge edge = adjList.get(i);
                MyNode nodeTo = edge.getNodeTo();
                double weiht = edge.getWeight();
                double wasteSize = nodeTo.getWasteReq(wasteType);
                double newEmptySpace = wasteSize >= emptySpace? 0:emptySpace-wasteSize;

                if (!openList.contains(nodeTo)) { // node is not in openList
                    nodeTo.setParent(current); // set current node as previous for this node
                    nodeTo.setState(distAtNow + weiht, newEmptySpace);
                    openList.add(nodeTo); // add node to openList
                }
                else { // node is in openList
                    MyNode newNode = new MyNode(-1, -1, -1, -1, -1);
                    newNode.setState(distAtNow + weiht, newEmptySpace);

                    if (newNode.getF(0.5, 0.5, spaceTruck) < nodeTo.getF(0.5, 0.5, spaceTruck)) { // costs from current node are cheaper than previous costs
                        nodeTo.setParent(current); // set current node as previous for this node
                        nodeTo.setState(distAtNow + weiht, newEmptySpace);
                    }
                }
            }

            if (openList.isEmpty()) { // no path exists
                return new LinkedList<MyNode>(); // return empty list
            }
        }
        return null; // unreachable
    }

    private MyNode getLowestF(LinkedList<MyNode> list, double spaceTruck) {
        MyNode cheapest = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getF(0.5, 0.5, spaceTruck) < cheapest.getF(0.5, 0.5, spaceTruck)) {
                cheapest = list.get(i);
            }
        }
        return cheapest;
    }

    private List<MyNode> calcPath(MyNode start, MyNode goal) {
        LinkedList<MyNode> path = new LinkedList<MyNode>();

        MyNode curr = goal;
        boolean done = false;
        while (!done) {
            path.addFirst(curr);
            curr = curr.getParent();

            if (curr.equals(start)) {
                done = true;
            }
        }
        return path;
    }
}
