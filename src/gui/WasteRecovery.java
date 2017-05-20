package gui;

import myGraph.MyGraph;
import myGraph.MyPath;
import myGraph.Solver;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import wasteManagement.Waste;
import wasteManagement.WasteManagement;

import javax.swing.*;
import java.awt.*;

public class WasteRecovery {
    private JPanel pane;
    private ViewPanel graphPanel;
    private JTextArea pathAnalysis;
    private JTextArea bfsInfo;
    private JTextArea astarInfo;
    private JTextArea dfsInfo;
    private JComboBox<String> truckNoVal;
    private JComboBox algorithmComboVal;
    private JButton backButton;
    private JButton nextTruckButton;
    private JButton previousTruckButton;
    private JLabel numberOfTrucks;
    private JTextArea astarBetaZeroInfo;

    private Solver astarSolver = null;
    private Solver astarBetaZeroSolver = null;
    private Solver dfsSolver = null;
    private Solver bfsSolver = null;
    private Solver currSolver = null;
    private MainWindow parent = null;

    Viewer viewer;

    boolean algorithChange = false;

    public WasteRecovery(MainWindow mainWindow) {
        this.parent = mainWindow;
        addListeners();
    }

    private void addListeners() {
        truckNoVal.addActionListener(actionEvent -> {
            if (algorithChange)
                return;

            int currTruck = Integer.parseInt(truckNoVal.getSelectedItem().toString()) - 1;
            printNewPath(currTruck);

            if(currTruck != 0){
                previousTruckButton.setEnabled(true);
            } else previousTruckButton.setEnabled(false);
            if (currSolver.getPath(currTruck+1) == null)
                nextTruckButton.setEnabled(false);
            else nextTruckButton.setEnabled(true);
        });

        nextTruckButton.addActionListener(actionEvent -> {
            int currTruck = Integer.parseInt(truckNoVal.getSelectedItem().toString()) - 1;
            updateCurrTruck(currTruck, 2);
        });

        previousTruckButton.addActionListener(actionEvent -> {
            int currTruck = Integer.parseInt(truckNoVal.getSelectedItem().toString()) - 1;
            updateCurrTruck(currTruck, 0);
        });

        algorithmComboVal.addActionListener (actionEvent -> {
            setCurrSolver();
            showCurrSolverInfo();

            if (currSolver.foundPaths())
                printNewPath(0);
        });
    }

    public void updateCurrTruck(int currTruck, int value){
        if(currTruck != 0){
            previousTruckButton.setEnabled(true);
        } else previousTruckButton.setEnabled(false);
        if (currSolver.getPath(currTruck+1) == null)
            nextTruckButton.setEnabled(false);
        else nextTruckButton.setEnabled(true);
        truckNoVal.setSelectedItem(Integer.toString(currTruck + value));
        printNewPath(currTruck + value - 1);
    }

    public void printNewPath(int pathNo) {
        Graph graph = parent.getGraph();
        MyGraph myGraph = currSolver.getGraph();

        myGraph.resetColorEdgeOfGraph(graph);
        MyPath nextPath = currSolver.getPath(pathNo);
        nextPath.printEdgesOfPath(graph, Color.red);
        pathAnalysis.setText(nextPath.getPathInfo());
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

    public void initSolvers(Graph graph, WasteManagement wasteManagement, String wasteType, double alfaValue, double betaValue, int numTrucks) throws Exception {
        setGraphPanel(graph);

        setSolvers(graph, wasteManagement, wasteType, alfaValue, betaValue, numTrucks);
        setCurrSolver();
        showCurrSolverInfo();

        MyGraph g = currSolver.getGraph();
        // label nodes with ids
        for (Node n: graph)
            n.addAttribute("ui.label", Integer.toString(n.getIndex()));

        // print nodes with waste
        g.printNodes(graph, g.getNodesWithACertainWaste(Waste.toEnum(wasteType)));

        // print initial path
        if (currSolver.foundPaths())
            printNewPath(0);

    }

    private void showCurrSolverInfo() {
        Graph graph = parent.getGraph();

        if (currSolver.foundPaths()) {
            numberOfTrucks.setText(currSolver.getNumTrucksUsed() + " trucks were used in the waste recovery.");
            previousTruckButton.setEnabled(false);
            nextTruckButton.setEnabled(true);

            algorithChange = true;
            truckNoVal.removeAllItems();
            for(int i = 0; i < currSolver.getNumTrucksUsed(); i++){
                truckNoVal.addItem(Integer.toString(i + 1));
            }
            truckNoVal.setSelectedIndex(0);
            algorithChange = false;
        }
        else {
            numberOfTrucks.setText("No trucks were used: Low number of residues in all containers.");
            previousTruckButton.setEnabled(false);
            nextTruckButton.setEnabled(false);
        }
    }

    private Solver setCurrSolver() {
        String algorithmOption = (String) algorithmComboVal.getSelectedItem();

        if (algorithmOption.equals("A*"))
            currSolver = astarSolver;
        else if (algorithmOption.equals("DFS"))
            currSolver = dfsSolver;
        else if (algorithmOption.equals("BFS"))
            currSolver = bfsSolver;
        else if (algorithmOption.equals("A* beta = 0"))
            currSolver = astarBetaZeroSolver;
        return null;
    }

    private void setSolvers(Graph graph, WasteManagement wasteManagement, String wasteType, double alfaValue, double betaValue, int numTrucks) throws Exception {
        astarSolver = new Solver(graph, wasteManagement, Waste.toEnum(wasteType), alfaValue, betaValue, numTrucks);
        astarSolver.solve("A*");
        astarInfo.setText(astarSolver.getInfo());

        astarBetaZeroSolver = new Solver(graph, wasteManagement, Waste.toEnum(wasteType), 1, 0, numTrucks);
        astarBetaZeroSolver.solve("A*");
        astarBetaZeroInfo.setText(astarBetaZeroSolver.getInfo());

        dfsSolver = new Solver(graph, wasteManagement, Waste.toEnum(wasteType), alfaValue, betaValue, numTrucks);
        dfsSolver.solve("dfs");
        dfsInfo.setText(dfsSolver.getInfo());

        bfsSolver = new Solver(graph, wasteManagement, Waste.toEnum(wasteType), alfaValue, betaValue, numTrucks);
        bfsSolver.solve("bfs");
        bfsInfo.setText(bfsSolver.getInfo());
    }

    private void createUIComponents() {
        Graph graph = parent.getGraph();
        setGraphPanel(graph);
    }

    public void setGraphPanel(Graph graph) {
        graph.setAutoCreate(true);
        graph.setStrict(false);

        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", "url(data/stylesheet.css)");

        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        graphPanel = viewer.addDefaultView(false);
    }

}
