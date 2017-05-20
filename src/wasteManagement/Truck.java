package wasteManagement;

public class Truck {
    private Waste residue;
    private int maxCapacity;

    public Truck(String residueType, int maxCapacity){
        residue = Waste.toEnum(residueType);
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getTruckDetails(){
        return residue.toString() + " truck; Maximum capacity: " + maxCapacity + "kg";
    }

    public void printTruckDetails() {
        System.out.println(getTruckDetails());
    }

}
