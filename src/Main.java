import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CityGraph cg = new CityGraph("graph1");

        WasteManagement management = null;
        try {
            management = new WasteManagement("station1", cg.getGraph());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        management.printManagementDetails();

        cg.display();

        // h = f + g
        // f = alfa * distância/distanciaMax (greedy) + beta * disponibilidade/capacidadeMax
    }
}
