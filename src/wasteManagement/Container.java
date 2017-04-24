package wasteManagement;

import org.graphstream.graph.Node;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static wasteManagement.Waste.HOUSEHOLD;

public class Container {
    private Node location;
    private ArrayList<Map.Entry<Waste, Double>> residues;

    public Container(Node n){
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

    public void addResidue(Waste residue, Double value){
        residues.add(new AbstractMap.SimpleEntry<>(residue, value));
    }

    public ArrayList<Map.Entry<Waste, Double>> getResidues() {
        return residues;
    }

    public void setResidues(ArrayList<Map.Entry<Waste, Double>> residues) {
        this.residues = residues;
    }
    public Node getLocation() {
        return location;
    }

    public void setLocation(Node location) {
        this.location = location;
    }

    public void printContainerDetails() {
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

    public ArrayList<Double> getContainerInfo(){
        ArrayList<Double> ret = new ArrayList<>();
        ret.add(Double.parseDouble(location.getId()));
        ret.add(0.0); ret.add(0.0); ret.add(0.0); ret.add(0.0);

        for(int i = 0; i < residues.size(); i++){
            Map.Entry<Waste, Double> entry = residues.get(i);
            Waste key = entry.getKey();
            Double value = entry.getValue();
            switch (key){
                case HOUSEHOLD:
                    ret.set(1, value);
                    break;
                case PAPER:
                    ret.set(2, value);
                    break;
                case PLASTIC:
                    ret.set(3, value);
                    break;
                case GLASS:
                    ret.set(4, value);
                    break;
            }
        }

        return ret;
    }

    public void updateResidue(Waste residue, double newValue) {
        for(int i = 0; i < residues.size(); i++){
            Map.Entry<Waste, Double> entry = residues.get(i);
            Waste key = entry.getKey();
            double value = entry.getValue();
            if(key.equals(residue)){
                entry.setValue(newValue + value);
                return;
            }
        }

        addResidue(residue, newValue);
    }

    public double setResidue(Waste residue, double newValue) {
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
