package wasteManagement;

import org.graphstream.graph.Node;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Container {
    private Node location;
    private ArrayList<Map.Entry<Waste, Double>> residues;

    Container(Node n){
        residues = new ArrayList<>();
        location = n;
        Map waste = n.getAttribute("waste");
        Iterator entries = waste.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            Object key = thisEntry.getKey();
            Object value = thisEntry.getValue();
            addResidue(Waste.toEnum(key.toString()), (double)value);
        }
    }

    private void addResidue(Waste residue, Double value){
        residues.add(new AbstractMap.SimpleEntry<>(residue, value));
    }

    ArrayList<Map.Entry<Waste, Double>> getResidues() {
        return residues;
    }

    public Node getLocation() {
        return location;
    }

    void printContainerDetails() {
        System.out.println("Container location: MyNode " + location.getId() + ".");
        System.out.println("    Residues:");
        for(int i = 0; i < residues.size(); i++){
            Map.Entry<Waste, Double> entry = residues.get(i);
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("        " + key + " -> " + value + "kg");
        }
        System.out.println();
    }

    ArrayList<Double> getContainerInfo(){
        ArrayList<Double> ret = new ArrayList<>();
        ret.add((double)location.getIndex());
        ret.add(0.0); ret.add(0.0); ret.add(0.0); ret.add(0.0);

        ret.set(1, this.getWasteOfAType(Waste.HOUSEHOLD));
        ret.set(2, this.getWasteOfAType(Waste.PAPER));
        ret.set(3, this.getWasteOfAType(Waste.PLASTIC));
        ret.set(4, this.getWasteOfAType(Waste.GLASS));

        return ret;
    }

    public double getWasteOfAType(Waste wasteType) {
        for(int i = 0; i < residues.size(); i++){
            Map.Entry<Waste, Double> entry = residues.get(i);
            Waste key = entry.getKey();
            Double value = entry.getValue();
            if (key == wasteType)
                return value;
        }
        return 0;
    }

    double setResidue(Waste residue, double newValue) {
        for(int i = 0; i < residues.size(); i++){
            Map.Entry<Waste, Double> entry = residues.get(i);
            Object key = entry.getKey();
            double value = entry.getValue();
            if(key.equals(residue)){
                entry.setValue(newValue);
                return newValue - value;
            }
        }

        addResidue(residue, newValue);
        return newValue;
    }
}
