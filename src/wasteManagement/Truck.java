package wasteManagement;

public class Truck {
    private Waste residue;
    private int maxCapacity;
    private int load;

    public Truck(String residueType, int maxCapacity){
        residue = Waste.toEnum(residueType);
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

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public String getTruckDetails(){
        return residue.toString() + " truck; Maximum capacity: " + maxCapacity + "kg";
    }

    public void printTruckDetails() {
        System.out.println(getTruckDetails());
    }

}
