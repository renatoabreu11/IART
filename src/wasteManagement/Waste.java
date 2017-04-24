package wasteManagement;

public enum Waste{
    HOUSEHOLD,
    PAPER,
    GLASS,
    PLASTIC;

    public String toString(){
        switch(this){
            case HOUSEHOLD :
                return "Household";
            case PAPER :
                return "Paper";
            case GLASS :
                return "Glass";
            case PLASTIC :
                return "Plastic";
        }
        return null;
    }

    public static Waste toEnum(String value){
        if(value.equalsIgnoreCase(HOUSEHOLD.toString()))
            return Waste.HOUSEHOLD;
        else if(value.equalsIgnoreCase(PAPER.toString()))
            return Waste.PAPER;
        else if(value.equalsIgnoreCase(GLASS.toString()))
            return Waste.GLASS;
        else if(value.equalsIgnoreCase(PLASTIC.toString()))
            return Waste.PLASTIC;
        else
            return null;
    }
}