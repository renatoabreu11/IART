package MyGraph;


import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

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

    /*
    public final List<MyNode> findPath(MyNode from, MyNode to) {

        LinkedList<MyNode> openList = new LinkedList<MyNode>();
        LinkedList<MyNode> closedList = new LinkedList<MyNode>();
        openList.add(from); // add starting node to open list

        boolean done = false;
        MyNode current = null;
        while (!done) {
            current = lowestFInOpen(); // get node with lowest fCosts from openList
            closedList.add(current); // add current node to closed list
            openList.remove(current); // delete current node from open list

            if ((current.getxPosition() == newX)
                    && (current.getyPosition() == newY)) { // found goal
                return calcPath(nodes[oldX][oldY], current);
            }

            // for all adjacent nodes:
            List<T> adjacentNodes = getAdjacent(current);
            for (int i = 0; i < adjacentNodes.size(); i++) {
                T currentAdj = adjacentNodes.get(i);
                if (!openList.contains(currentAdj)) { // node is not in openList
                    currentAdj.setPrevious(current); // set current node as previous for this node
                    currentAdj.sethCosts(nodes[newX][newY]); // set h costs of this node (estimated costs to goal)
                    currentAdj.setgCosts(current); // set g costs of this node (costs from start to this node)
                    openList.add(currentAdj); // add node to openList
                } else { // node is in openList
                    if (currentAdj.getgCosts() > currentAdj.calculategCosts(current)) { // costs from current node are cheaper than previous costs
                        currentAdj.setPrevious(current); // set current node as previous for this node
                        currentAdj.setgCosts(current); // set g costs of this node (costs from start to this node)
                    }
                }
            }

            if (openList.isEmpty()) { // no path exists
                return new LinkedList<T>(); // return empty list
            }
        }
        return null; // unreachable
    }

    private MyNode getLowestNode(LinkedList<MyNode> list) {
        // TODO currently, this is done by going through the whole openList!
        MyNode cheapest = list.get(0);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getfCosts() < cheapest.getfCosts()) {
                cheapest = list.get(i);
            }
        }
        return cheapest;
    }*/
}
