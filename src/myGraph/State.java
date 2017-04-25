package myGraph;

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
    double wasteNeighboringNodes;
    List<Double> distsWasteStation;
    List<MyNode> nodesThatCollectWaste;

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
        this.nodesThatCollectWaste = new ArrayList<MyNode>();
    }

    public double getF(double alfa, double beta) {
        double collected = spaceTruck - emptySpace;
        double g = alfa * (distAtNow / edgesCostSum) - beta * collected / spaceTruck * 1.5;
        double h = distToStation / edgesCostSum * alfa - beta * wasteNeighboringNodes / wasteSum;
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

    public double getWasteNeighboringNodes() {
        return wasteNeighboringNodes;
    }

    public void setWasteNeighboringNodes(double wasteNeighboringNodes) {
        this.wasteNeighboringNodes = wasteNeighboringNodes;
    }

    public List<State> getAdjList() {
        List<State> adj = new ArrayList<State>();

        List<MyEdge> edges = node.getAdjList();
        for (MyEdge edge: edges) {

            MyNode nodeTo = edge.getNodeTo();

            double newEmptySpace = emptySpace;
            double newDistAtNow = edge.getWeight() + distAtNow;
            List<MyNode> nodesThatCollectWasteAux = new ArrayList<MyNode>(nodesThatCollectWaste);
            if (!nodesThatCollectWaste.contains(nodeTo)) {
                nodesThatCollectWasteAux.add(nodeTo);
                double wasteSize = nodeTo.getWasteReq(wasteType);
                newEmptySpace = wasteSize >= emptySpace? 0:emptySpace-wasteSize;
            }
            double wasteNeig = nodeTo.getWasteNeighboringNodes(nodesThatCollectWasteAux, wasteType);
            State newState = new State(nodeTo, newDistAtNow, edgesCostSum, wasteSum, newEmptySpace, spaceTruck, distsWasteStation, wasteType);
            newState.setNodesThatCollectWaste(nodesThatCollectWasteAux);
            newState.setWasteNeighboringNodes(wasteNeig);

            adj.add(newState);
        }

        return adj;
    }

    public void setParent(State parent) {
        this.parent = parent;
    }

    public void setNodesThatCollectWaste(List<MyNode> nodesThatCollectWaste) {
        this.nodesThatCollectWaste = new ArrayList<MyNode>(nodesThatCollectWaste);
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
        res += "node: " + node.getId() + " f* = " + this.getF(0.5, 0.5) + " emptySpc: " +
                this.getEmptySpace() + " dist: " + this.getDistAtNow() + ";;;";
        return res;
    }


}
