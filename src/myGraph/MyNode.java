package myGraph;

import wasteManagement.Waste;

import java.util.ArrayList;
import java.util.List;

public class MyNode implements Comparable {

    private final int id;

    private double paper;
    private double plastic;
    private double glass;
    private double household;

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


    public double getWasteNeighboringNodes(List<MyNode> nodesThatCollectWaste, Waste typeWaste) {
        double ans = 0;
        for (MyEdge v: adjList)
            if (!nodesThatCollectWaste.contains(v.getNodeTo()))
                ans += v.getNodeTo().getWasteReq(typeWaste);
        return ans;
    }

    public void updateWaste(Waste typeWaste, double wasteCollected) {
        switch (typeWaste) {
            case GLASS:
                glass -= wasteCollected;
            case HOUSEHOLD:
                household -= wasteCollected;
            case PAPER:
                paper -= wasteCollected;
            case PLASTIC:
                plastic -= wasteCollected;
        }
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

    @Override
    public int compareTo(Object o) {
        int id1 = this.id;
        int id2 = ((MyNode)o).getId();
        if (id1 < id2)
            return -1;
        else if (id1 > id2)
            return 1;
        else
            return 0;
    }
}
