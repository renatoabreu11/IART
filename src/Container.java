import org.graphstream.graph.Node;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

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
            addResidue(key.toString(), (double)value);
        }
    }

    public void addResidue(String type, Double value){
        Waste residue;
        switch (type){
            case "plastic":
                residue = Waste.PLASTIC; break;
            case "paper":
                residue = Waste.PAPER; break;
            case "glass":
                residue = Waste.GLASS; break;
            case "household":
                residue = Waste.HOUSEHOLD; break;
            default: residue = Waste.HOUSEHOLD; break;
        }
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
}
