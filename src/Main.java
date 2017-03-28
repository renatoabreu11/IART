import org.graphstream.algorithm.AStar;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CityGraph cg = new CityGraph("graph1");

        Station wasteStation = null;
        try {
            wasteStation = new Station("station1", cg.getGraph());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        wasteStation.printStationDetails();

        AStar astar = new AStar(cg.getGraph());
        astar.setCosts(new AStar.DistanceCosts());
        astar.compute("0", "0");

        System.out.println(astar.getShortestPath());

        cg.display();

        // h = f + g
        // f = alfa * distância/distanciaMax (greedy) + beta * disponibilidade/capacidadeMax
    }
}
