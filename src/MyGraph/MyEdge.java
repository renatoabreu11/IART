package MyGraph;

public class MyEdge {

    private final MyNode from;
    private final MyNode to;

    private final double weight;

    public MyEdge(MyNode from, MyNode to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
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

}
