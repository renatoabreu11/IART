import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PathCosts implements AStar.Costs {

    private double alfa = 0.5;
    private double beta = 0.5;

    // distancia total começando da estação, percorrer todos os contentores e retornar à estação)
    private double max_dist = 20;

    private Waste typeWaste;

    public PathCosts(double alfa, double beta, double max_dist, Waste typeWaste) {
        this.alfa = alfa;
        this.beta = beta;
        this.max_dist = max_dist;
        this.typeWaste = typeWaste;
    }

    /**
     *h (custo esperado) = alfa * (distancia minima até à estação) + beta * (lixo restante / lixo inicial)
     */
    @Override
    public double heuristic(Node node, Node target) {

        return 0;
    }

    /**
     * g (custo ate ao momento) = alfa * (distanciaAteAoMomento) / (distancia total começando da estação, percorrer todos os contentores e retornar à estação)
         + beta * (disp / carga)
     */
    @Override
    public double cost(Node parent, Edge from, Node next) {

        String typeWasteStr = null;
        if (typeWaste == Waste.GLASS) {
            typeWasteStr = "glass";
        }
        else if (typeWaste == Waste.HOUSEHOLD) {
            typeWasteStr = "household";
        }
        else if (typeWaste == Waste.PAPER) {
            typeWasteStr = "paper";
        }
        else if (typeWaste == Waste.PLASTIC) {
            typeWasteStr = "plastic";
        }

        double c1 = alfa * (double)from.getAttribute("weight") / max_dist;

        if (next.hasAttribute("waste")) {

            Map waste = next.getAttribute("waste");
            Iterator entries = waste.entrySet().iterator();

            while (entries.hasNext()) {
                Map.Entry thisEntry = (Map.Entry) entries.next();

                String wasteType = (String)thisEntry.getKey();

                int wasteQuant = (int)thisEntry.getValue();

                if (wasteType.equals(typeWasteStr)) {


                }
            }

        }

        return 0;
    }

}
