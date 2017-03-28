import org.graphstream.graph.Node;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Container {
    private Node location;
    private ArrayList<Map.Entry<Waste, Integer>> residues;

    public Container(Node n){
        residues = new ArrayList<>();
        location = n;
        Map waste = n.getAttribute("waste");
        Iterator entries = waste.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            Object key = thisEntry.getKey();
            Object value = thisEntry.getValue();
            addResidue(key.toString(), (int)value);
        }
    }

    public void addResidue(String type, Integer value){
        Waste residue;
        switch (type){
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
        residues.add(new AbstractMap.SimpleEntry<>(residue, value));
    }

    public Node getLocation() {
        return location;
    }

    public void setLocation(Node location) {
        this.location = location;
    }

    public void printContainerDetails() {
        System.out.println("Container location: Node " + location.getId() + ".");
        System.out.println("    Residues:");
        Map waste = location.getAttribute("waste");
        Iterator entries = waste.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            Object key = thisEntry.getKey();
            Object value = thisEntry.getValue();
            System.out.println("        " + key + " -> " + value + "kg");
        }
        System.out.println();
    }
}
