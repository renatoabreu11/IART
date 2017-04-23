package myGraph;

public class MyEdge {

    private final MyNode from;
    private final MyNode to;

    private final double weight;
    private final int index;

    public MyEdge(MyNode from, MyNode to, double weight, int index) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.index = index;
    }

    public double getWeight() {
        return weight;
    }

    public MyNode getNodeFrom() {
        return from;
    }

    public MyNode getNodeTo() {
        return to;
    }

    public int getIndex() {return index;}

}
