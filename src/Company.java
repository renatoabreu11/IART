import org.graphstream.graph.Node;

import java.util.ArrayList;

public class Company {
    private ArrayList<Truck> trucks;
    private Node station;

    public Company(){
        trucks = new ArrayList<>();
    }

    public ArrayList<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(ArrayList<Truck> trucks) {
        this.trucks = trucks;
    }

    public Node getStation() {
        return station;
    }

    public void setStation(Node station) {
        this.station = station;
    }
}
