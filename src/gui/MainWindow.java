package gui;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.ViewerListener;
import org.xml.sax.SAXException;
import wasteManagement.WasteManagement;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class MainWindow extends JFrame implements ViewerListener{
    private MainOptions mainOptions;
    private WasteOptions wasteOptions;
    private Management management;
    private Statistics statistics;
    private WasteRecovery wasteRecovery;
    private JPanel contentPane;

    private Graph graph;
    private String graphFile;
    private WasteManagement wasteManagement = null;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainWindow frame = new MainWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainWindow() {
        setTitle("Waste Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel(new CardLayout());
        setContentPane(contentPane);
        initialize();
        setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        mainOptions = new MainOptions();
        mainOptions.setVisible(true);

        management = new Management(this);
        management.setVisible(true);

        wasteOptions = new WasteOptions(this);
        wasteOptions.setVisible(true);

        statistics = new Statistics(this);
        statistics.setVisible(true);

        wasteRecovery = new WasteRecovery(this);
        wasteRecovery.setVisible(true);

        contentPane.add(mainOptions.getPane(), "Main Options");
        contentPane.add(wasteOptions.getPane(), "Waste Options");
        contentPane.add(management.getPane(), "Waste Management");
        contentPane.add(statistics.getPane(), "Statistics");
        contentPane.add(wasteRecovery.getPane(), "Waste Recovery");

        addListeners();

        CardLayout cardLayout = (CardLayout) contentPane.getLayout();
        cardLayout.show(contentPane, "Main Options");
    }

    private void addListeners(){
        mainOptions.getOptionsButton().addActionListener(e -> showLayout("Waste Options"));

        mainOptions.getWasteRecoveryButton().addActionListener(e -> showLayout("Waste Management"));

        wasteOptions.getBackButton().addActionListener(e -> showLayout("Main Options"));

        management.getBackButton().addActionListener(e -> showLayout("Main Options"));

        statistics.getBackButton().addActionListener(e -> showLayout("Waste Management"));

        wasteRecovery.getBackButton().addActionListener(e -> showLayout("Waste Management"));
    }

    public void showLayout(String layout){
        CardLayout cardLayout = (CardLayout) contentPane.getLayout();
        cardLayout.show(contentPane, layout);
    }

    public Statistics getStatistics(){
        return statistics;
    }

    public void initWasteManagement(String graph, String station){
        this.graph = new SingleGraph(graph);
        graphFile = graph;
        try {
            this.graph.read("data/"+graphFile);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            wasteManagement = new WasteManagement(station, this.graph);
        } catch (IOException | SAXException | ParserConfigurationException e1) {
            e1.printStackTrace();
        }
    }

    public WasteManagement getWasteManagement(){
        return wasteManagement;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setWasteManagement(WasteManagement wasteManagement) {
        this.wasteManagement = wasteManagement;
    }

    public void setGraph(String graph) {
        this.graph = new SingleGraph(graph);
        graphFile = graph;
        try {
            this.graph.read("data/"+graphFile);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void updateWasteManagement() {
        wasteManagement.update(this.graph);
    }

    public void updateGraphPanel() {
        this.wasteRecovery.setGraphPanel(this.graph);
    }

    public void initWasteRecovery() {
        String wasteType = (String)wasteOptions.getWasteCollection().getSelectedItem();
        double alfaValue = (double)wasteOptions.getAlfaValue().getValue();
        double betaValue = (double)wasteOptions.getBetaValue().getValue();
        int numTrucks = (int)wasteOptions.getMaxTrucks().getValue();
        try {
            this.wasteRecovery.initSolvers(this.graph, wasteManagement, wasteType, alfaValue, betaValue, numTrucks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void viewClosed(String s) {

    }

    @Override
    public void buttonPushed(String id) {
        System.out.println("Button pushed on node "+id);
        Node node = this.graph.getNode(id);
        if(node.hasAttribute("waste")){
            Map waste = node.getAttribute("waste");
            Iterator entries = waste.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry thisEntry = (Map.Entry) entries.next();
                Object key = thisEntry.getKey();
                Object value = thisEntry.getValue();
                System.out.println("Type of residue: " + key +" -> " + value + " kg");
            }
        }

    }

    @Override
    public void buttonReleased(String s) {

    }

    public String getGraphFile() {
        return graphFile;
    }
}
