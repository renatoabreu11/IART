package gui;

import myGraph.MyPath;
import myGraph.Solver;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.xml.sax.SAXException;
import wasteManagement.CityGraph;
import wasteManagement.Waste;
import wasteManagement.WasteManagement;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Teste {
    private JPanel panel1;
    private JButton button1;
    private ViewPanel viewPanel1;
    private JFrame frame;

    Graph graph;
    WasteManagement management;
    CityGraph cityGraph;

    public Teste() {
        frame = new JFrame();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel1);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        Teste teste = new Teste();
        teste.updateGraph();
    }

    public void updateGraph() throws Exception {
        Solver astarSolver = new Solver(graph, management, Waste.GLASS, 0.5, 0.5, 2);
        astarSolver.solve("A*");
        java.util.List<MyPath> sol = astarSolver.getSolution();
        sol.get(0).printEdgesOfPath(graph, Color.red);

        panel1.repaint();
        viewPanel1.repaint();
        panel1.revalidate();
        viewPanel1.revalidate();

        /*
        viewPanel1.openInAFrame(false);
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        viewPanel1 = viewer.addDefaultView(true);   // false indicates "no JFrame"
        viewPanel1.openInAFrame(true);*/
    }

    private void createUIComponents() {
        cityGraph = new CityGraph("graph1.dgs");
        management = null;
        try {
            management = new WasteManagement("station1.xml", cityGraph.getGraph());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        management.printManagementDetails();
        graph = cityGraph.getGraph();
        graph.setAutoCreate(true);
        graph.setStrict(false);

        Viewer viewer = graph.display();
        viewer.enableAutoLayout();
        viewPanel1 = viewer.addDefaultView(true);   // false indicates "no JFrame"
        viewPanel1.resizeFrame(400, 400);
    }
}
