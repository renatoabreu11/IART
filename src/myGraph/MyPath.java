package myGraph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import wasteManagement.Waste;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MyPath {

    List<MyNode> path;
    List<Double> wasteCollectedInEachNode;

    double sumWasteCollected;
    double distPercorred;
    Waste typeWaste;


    public MyPath(List<MyNode> path, double capacityTruck, Waste typeWaste) {
        this.path = path;
        this.typeWaste = typeWaste;
        this.setWasteCollectedInEachNodeAndSumWasteCollected(capacityTruck);
        this.setDistPercorred();
    }

    double getDistPercorred() {
        return distPercorred;
    }

    double getSumWasteCollected() {
        return sumWasteCollected;
    }

    private void setWasteCollectedInEachNodeAndSumWasteCollected(double capacityTruck) {

        wasteCollectedInEachNode = new ArrayList<Double>();
        sumWasteCollected = 0;
        Set<MyNode> visitedNodes = new TreeSet<MyNode>();
        for (MyNode node: path) {
            if (!visitedNodes.contains(node)) {
                visitedNodes.add(node);
                double wasteInNode = node.getWasteReq(typeWaste);

                if (sumWasteCollected + wasteInNode > capacityTruck) {
                    wasteCollectedInEachNode.add(capacityTruck - sumWasteCollected);
                    sumWasteCollected = capacityTruck;
                }
                else {
                    wasteCollectedInEachNode.add(wasteInNode);
                    sumWasteCollected += wasteInNode;
                }
            }
            else {
                wasteCollectedInEachNode.add(0d);
            }
        }

    }

    private void setDistPercorred() {
        distPercorred = 0;
        List<MyEdge> edges = this.getEdgesOfPath();
        for (MyEdge e: edges)
            distPercorred += e.getWeight();
    }

    List<MyNode> getNodesOfPath() {
        return path;
    }

    public String getPathInfo() {
        String info = "Path info: idNode1(waste collected1) -> idNode2(waste collected2) -> ...\n\n";
        for (int i = 0; i < path.size(); i++) {
            int idNode = path.get(i).getId();
            double waste = wasteCollectedInEachNode.get(i);
            info += idNode + "(" + waste + ")";
            if (i < path.size()-1) {
                if (i % 4 == 0 && i != 0) {
                    info += " -> \n-> ";
                }
                else {
                    info += " -> ";
                }
            }
            else {
                info += "\n";
            }
        }
        return info;
    }

    private List<MyEdge> getEdgesOfPath() {
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

    public void printEdgesOfPath(Graph g, Color color) {
        List<MyEdge> edgesOfPath = this.getEdgesOfPath();
        for (MyEdge e: edgesOfPath)
            g.getEdge(e.getIndex()).addAttribute("ui.color", color);
    }

    public void printPath() {
        System.out.println("Number of nodes of path: " + path.size());
        for (int i = 0; i < path.size(); i++) {
            MyNode node = path.get(i);
            System.out.print(node.getId());
            if (i < path.size()-1) {
                System.out.print(" -> ");
            }
            else {
                System.out.println();
            }
        }
        System.out.println("Glass collected: " + sumWasteCollected);
        System.out.println("Distance percorred: " + distPercorred);
    }

    public void updateWasteInNodes() {
        for (int i = 0; i < path.size(); i++) {
            MyNode node = path.get(i);
            double wasteCollectedInNode = wasteCollectedInEachNode.get(i);
            node.updateWaste(typeWaste, wasteCollectedInNode);
        }
    }

}
