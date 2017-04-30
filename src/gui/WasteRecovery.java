package gui;

import myGraph.Solver;
import org.graphstream.graph.Graph;
import wasteManagement.Waste;
import wasteManagement.WasteManagement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WasteRecovery {
    private JPanel pane;
    private JButton backButton;
    private JList astar;
    private JList dfs;
    private JList bfs;
    private JButton runBfsButton;
    private JButton stepDfsButton;
    private JButton stepAstarButton;
    private JButton stepBfsButton;
    private JButton runDfsButton;
    private JButton runAstarButton;
    private JTextArea astarInfo;
    private JTextArea dfsInfo;
    private JTextArea bfsInfo;
    private MainWindow parent;

    private Solver astarSolver = null;
    private Solver dfsSolver = null;
    private Solver bfsSolver = null;

    public WasteRecovery(MainWindow mainWindow) {
        this.parent = mainWindow;
        addListeners();
    }

    private void addListeners() {

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
        astarSolver = new Solver(graph, wasteManagement, Waste.GLASS, alfaValue, betaValue, 2);
        astarSolver.solve("A*");

        // alfa e beta são desnecessários aqui
        dfsSolver = new Solver(graph, wasteManagement, Waste.GLASS, alfaValue, betaValue, 2);
        dfsSolver.solve("dfs");

        bfsSolver = new Solver(graph, wasteManagement, Waste.GLASS, alfaValue, betaValue, 2);
        bfsSolver.solve("bfs");
    }
}