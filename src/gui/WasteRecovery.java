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
import java.util.ArrayList;

public class WasteRecovery {
    private JPanel pane;
    private JButton backButton;
    private JButton prevTruckBtn;
    private JButton nextTruckBtn;
    private JTextArea astarInfo;
    private JTextArea dfsInfo;
    private JTextArea bfsInfo;
    private ViewPanel graphPanel;
    private JComboBox algorithmComboVal;
    private JLabel numberOfTrucks;
    private JComboBox<String> truckNoVal;
    private MainWindow parent;

    private Solver astarSolver = null;
    private Solver dfsSolver = null;
    private Solver bfsSolver = null;
    private Solver currSolver = null;

    Viewer viewer;

    public WasteRecovery(MainWindow mainWindow) {
        this.parent = mainWindow;
        addListeners();
    }

    private void addListeners() {
        truckNoVal.addActionListener(actionEvent -> {
            int currTruck = Integer.parseInt(truckNoVal.getSelectedItem().toString()) - 1;
            printNewPath(currTruck);

            if(currTruck != 0){
                prevTruckBtn.setEnabled(true);
            } else prevTruckBtn.setEnabled(false);
            if (currSolver.getPath(currTruck+1) == null)
                nextTruckBtn.setEnabled(false);
            else nextTruckBtn.setEnabled(true);
        });

        nextTruckBtn.addActionListener(actionEvent -> {
            int currTruck = Integer.parseInt(truckNoVal.getSelectedItem().toString()) - 1;
            updateCurrTruck(currTruck, 2);
        });

        prevTruckBtn.addActionListener(actionEvent -> {
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
            prevTruckBtn.setEnabled(true);
        } else prevTruckBtn.setEnabled(false);
        if (currSolver.getPath(currTruck+1) == null)
            nextTruckBtn.setEnabled(false);
        else nextTruckBtn.setEnabled(true);
        truckNoVal.setSelectedItem(Integer.toString(currTruck + value));
        printNewPath(currTruck);
    }

    public void printNewPath(int pathNo) {
        Graph graph = parent.getCityGraph().getGraph();
        MyGraph myGraph = currSolver.getGraph();

        myGraph.resetColorEdgeOfGraph(graph);
        MyPath nextPath = currSolver.getPath(pathNo);
        nextPath.printEdgesOfPath(graph, Color.red);
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
        setGraphPanel(graph);

        setSolvers(graph, wasteManagement, wasteType, alfaValue, betaValue, trucks);
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
        Graph graph = parent.getCityGraph().getGraph();

        if (currSolver.foundPaths()) {
            numberOfTrucks.setText(currSolver.getNumTrucksUsed() + " trucks were used in the waste recovery.");
            prevTruckBtn.setEnabled(false);
            nextTruckBtn.setEnabled(true);

            truckNoVal.removeAllItems();
            for(int i = 0; i < currSolver.getNumTrucksUsed(); i++){
                truckNoVal.addItem(Integer.toString(i + 1));
            }
        }
        else {
            numberOfTrucks.setText("No trucks were used: Low number of residues in all containers.");
            prevTruckBtn.setEnabled(false);
            nextTruckBtn.setEnabled(false);
        }
    }

    private Solver setCurrSolver() {
        String algorithmOption = algorithmComboVal.getSelectedItem().toString();

        if (algorithmOption.equals("A*"))
            currSolver = astarSolver;
        else if (algorithmOption.equals("DFS"))
            currSolver = dfsSolver;
        else if (algorithmOption.equals("BFS"))
            currSolver = bfsSolver;
        return null;
    }

    private void setSolvers(Graph graph, WasteManagement wasteManagement, String wasteType, double alfaValue, double betaValue, ArrayList trucks) throws Exception {
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
    }

    private void createUIComponents() {
        Graph graph = parent.getCityGraph().getGraph();
        setGraphPanel(graph);
    }

    public void setGraphPanel(Graph graph) {
        graph.setAutoCreate(true);
        graph.setStrict(false);

        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", "url(data/stylesheet.css)");

        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        graphPanel = viewer.addDefaultView(false);
    }
}