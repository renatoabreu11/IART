package MyGraph;

import wasteManagement.Waste;

import java.util.ArrayList;

public class MyNode {

    private final int id;

    private final double paper;
    private final double plastic;
    private final double glass;
    private final double household;

    private ArrayList<MyEdge> adjList = new ArrayList<MyEdge>();

    // state
    private double distAtNow = Double.MAX_VALUE;
    private double emptySpace = Double.MAX_VALUE;

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

    public double getDistAtNow() {
        return distAtNow;
    }

    public double getEmptySpace() {
        return emptySpace;
    }

    public void setState(double distAtNow, double emptySpace) {
        this.distAtNow = distAtNow;
        this.emptySpace = emptySpace;
    }

    public double getF(double alfa, double beta, double spaceTruck) {

        // 20 = distancia (maxima?) da central ate wasteStation. TODO: fazer dfs que faça isso ou  mexer no alfa ?
        double g = alfa * (distAtNow / 20) + beta * (emptySpace / spaceTruck);

        // 20 =  distancia minima até à estação // TODO: fazer dikjstra que faça isso
        double h = (alfa + beta) * (20) + beta * 0; // 0 = na melhor das hipoteses o truck terá zero espaço vazio

        return g + h;
    }

    public double getG(double alfa, double beta, double spaceTruck) {
        double g = alfa * (distAtNow / 20) + beta * (emptySpace / spaceTruck);
        return g;
    }

    public void setParent(MyNode parent) {
        this.parent = parent;
    }

    public MyNode getParent() {
        return parent;
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
}
