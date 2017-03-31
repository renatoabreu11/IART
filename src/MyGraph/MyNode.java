package MyGraph;

import java.util.ArrayList;

public class MyNode {

    private final int id;

    private final double paper;
    private final double plastic;
    private final double glass;
    private final double household;

    private ArrayList<MyEdge> adjList = new ArrayList<MyEdge>();

    // state
    private int distAtNow = Integer.MAX_VALUE;
    private int emptySpace = Integer.MAX_VALUE;

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

    public ArrayList<MyEdge> getAdjList() {
        return adjList;
    }

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
}
