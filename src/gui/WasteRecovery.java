package gui;

import myGraph.MyGraph;
import myGraph.MyPath;
import myGraph.Solver;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicEdge;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import wasteManagement.Waste;
import wasteManagement.WasteManagement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WasteRecovery {
    private JPanel pane;
    private JButton backButton;
    private JButton setBtn;
    private JButton runBtn;
    private JTextArea astarInfo;
    private JTextArea dfsInfo;
    private JTextArea bfsInfo;
    private ViewPanel viewPanel1;
    private JComboBox comboBox1;
    private MainWindow parent;

    private Solver astarSolver = null;
    private Solver dfsSolver = null;
    private Solver bfsSolver = null;

    Viewer viewer;

    public WasteRecovery(MainWindow mainWindow) {
        this.parent = mainWindow;
        addListeners();
    }

    private void addListeners() {
        runBtn.addActionListener(actionEvent -> {
            System.out.println("oi");
            List<MyPath> sol = astarSolver.getSolution();
            sol.get(0).printEdgesOfPath(parent.getCityGraph().getGraph(), Color.red);
            //viewPanel1.display(viewer.getGraphicGraph(), true);
            viewPanel1.repaint();
        });
        setBtn.addActionListener(actionEvent -> {

        });
    }

    public JPanel getPane() {
        return pane;
    }

    public void setPane(JPanel pane) {
        this.pane = pane;
    }

    public void setVisible(boolean b) {
        this.pane.setVisible(b);
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void setBackButton(JButton backButton) {
        this.backButton = backButton;
    }

    public void initSolvers(Graph graph, WasteManagement wasteManagement, String wasteType, double alfaValue, double betaValue, ArrayList trucks) throws Exception {

        // recolher todo o lixo possivel -> wasteType default. aceitar lista de string com os camioes disponiveis.
        astarSolver = new Solver(graph, wasteManagement, Waste.toEnum(wasteType), alfaValue, betaValue, 2);
        astarSolver.solve("A*");
        astarInfo.setText(astarSolver.getInfo());

        // alfa e beta são desnecessários aqui
        dfsSolver = new Solver(graph, wasteManagement, Waste.toEnum(wasteType), alfaValue, betaValue, 2);
        dfsSolver.solve("dfs");
        dfsInfo.setText(dfsSolver.getInfo());

        bfsSolver = new Solver(graph, wasteManagement, Waste.toEnum(wasteType), alfaValue, betaValue, 2);
        bfsSolver.solve("bfs");
        bfsInfo.setText(bfsSolver.getInfo());

        // label nodes with ids
        for (Node n: graph)
            n.addAttribute("ui.label", Integer.toString(n.getIndex()));

        // print nodes with waste
        MyGraph g = astarSolver.getGraph();
        g.printNodes(graph, g.getNodesWithACertainWaste(Waste.toEnum(wasteType)));

        // print path
        List<MyPath> sol = astarSolver.getSolution();
        sol.get(0).printEdgesOfPath(graph, Color.red);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        Graph graph = parent.getCityGraph().getGraph();
        graph.setAutoCreate(true);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", "url(data/stylesheet.css)");

        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        //viewer.enableAutoLayout();
        viewPanel1 = viewer.addDefaultView(true);   // false indicates "no JFrame".
    }
}