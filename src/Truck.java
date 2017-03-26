import org.graphstream.graph.Node;

public class Truck {
    private Waste residue;
    private int maxCapacity;
    private int load;
    private Node location;

    public Truck(String residueType, int maxCapacity){
        switch (residueType){
            case "Plastic":
                residue = Waste.PLASTIC; break;
            case "Paper":
                residue = Waste.PAPER; break;
            case "Glass":
                residue = Waste.GLASS; break;
            case "Household":
                residue = Waste.HOUSEHOLD; break;
            default: residue = Waste.HOUSEHOLD; break;
        }

        this.maxCapacity = maxCapacity;
        this.load = 0;
    }

    public Waste getResidue() {
        return residue;
    }

    public void setResidue(Waste residue) {
        this.residue = residue;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Node getLocation() {
        return location;
    }

    public void setLocation(Node location) {
        this.location = location;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }
}
