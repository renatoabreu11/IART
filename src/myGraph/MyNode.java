package myGraph;

import wasteManagement.Waste;

import java.util.ArrayList;

public class MyNode {

    private final int id;

    private final double paper;
    private final double plastic;
    private final double glass;
    private final double household;

    private ArrayList<MyEdge> adjList = new ArrayList<MyEdge>();

    private boolean visited = false;
    private MyNode parent = null;

    public MyNode(int id, double paper, double plastic, double glass, double household) {
        this.id = id;
        this.paper = paper;
        this.plastic = plastic;
        this.glass = glass;
        this.household = household;
    }

    public void addEdge(MyEdge edge) {
        adjList.add(edge);
    }

    public int getId() {
        return id;
    }

    public ArrayList<MyEdge> getAdjList() {return adjList;}

    public double getHousehold() {
        return household;
    }

    public double getGlass() {
        return glass;
    }

    public double getPaper() {
        return paper;
    }

    public double getPlastic() {
        return plastic;
    }

    public double getWasteReq(Waste typeWaste) {
        switch (typeWaste) {
            case GLASS:
                return glass;
            case HOUSEHOLD:
                return household;
            case PAPER:
                return paper;
            case PLASTIC:
                return plastic;
        }
        return 0;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setParent(MyNode parent) {
        this.parent = parent;
    }

    public MyNode getParent() {
        return parent;
    }

    public boolean getVisited() {
        return visited;
    }
}
