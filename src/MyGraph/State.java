package MyGraph;

import wasteManagement.Waste;

import java.util.ArrayList;
import java.util.List;

public class State {

    MyNode node;

    double distAtNow;
    double edgesCostSum;
    double wasteSum;
    double emptySpace;
    double spaceTruck;
    double distToStation;
    List<Double> distsWasteStation;

    State parent = null;
    Waste wasteType;

    public State(MyNode node, double distAtNow, double edgesCostSum, double wasteSum, double emptySpace, double spaceTruck, List<Double> distsWasteStation, Waste wasteType) {
        this.node = node;
        this.distAtNow = distAtNow;
        this.edgesCostSum = edgesCostSum;
        this.emptySpace = emptySpace;
        this.distsWasteStation = distsWasteStation;
        this.wasteType = wasteType;
        this.distToStation = distsWasteStation.get(node.getId());
        this.spaceTruck = spaceTruck;
        this.wasteSum = wasteSum;
    }

    public double getF(double alfa, double beta) {
        double g = alfa * (distAtNow / edgesCostSum) + beta * (-(spaceTruck-emptySpace) / wasteSum);
        double h = distToStation / edgesCostSum * alfa;
        return g + h;
    }

    public int getIdOfNode() {
        return node.getId();
    }

    public double getDistAtNow() {
        return distAtNow;
    }

    public double getEmptySpace() {
        return emptySpace;
    }

    public List<State> getAdjList() {
        List<State> adj = new ArrayList<State>();

        List<MyEdge> edges = node.getAdjList();
        for (MyEdge edge: edges) {
            MyNode nodeTo = edge.getNodeTo();
            double weiht = edge.getWeight();
            double wasteSize = nodeTo.getWasteReq(wasteType);
            double newEmptySpace = wasteSize >= emptySpace? 0:emptySpace-wasteSize;
            double distToWasteStation = distsWasteStation.get(nodeTo.getId());
            double newDistAtNow = weiht + distAtNow;
            State newState = new State(nodeTo, newDistAtNow, edgesCostSum, wasteSum, newEmptySpace, spaceTruck, distsWasteStation, wasteType);
            adj.add(newState);
        }

        return adj;
    }

    public void setParent(State parent) {
        this.parent = parent;
    }

    public State getParent() {
        return parent;
    }

    public MyNode getNode() {
        return node;
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof State) {
            sameSame = (this.getIdOfNode() == ((State) object).getIdOfNode() &&
                        this.getEmptySpace() == ((State) object).getEmptySpace() &&
                        this.getDistAtNow() == ((State) object).getDistAtNow());
        }

        return sameSame;
    }

    @Override
    public String toString() {
        String res = "";
        res += "node: " + node.getId() + "\n";
        res += "dist at now: " + distAtNow + "\n";
        res += "empty space: " + emptySpace + "\n";
        res += "dist to station:" + distToStation + "\n";
        res += "edges sum:" + edgesCostSum + "\n";
        res += "space truck:" + spaceTruck + "\n";
        res += "f* = " + this.getF(0.5, 0.5) + "\n";
        return res;
    }


}
