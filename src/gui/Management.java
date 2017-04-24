package gui;

import org.xml.sax.SAXException;
import wasteManagement.CityGraph;
import wasteManagement.WasteManagement;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Management {
    private MainWindow parent;
    private JPanel pane;
    private JButton backButton;
    private JButton analysisButton;
    private JButton endShiftButton;
    private JButton wasteCollectionButton;

    private CityGraph cg = null;
    private WasteManagement wasteManagement = null;

    public Management(MainWindow mainWindow) {
        this.parent = mainWindow;
        addListeners();

        cg = new CityGraph("graph1");

        wasteManagement = null;
        try {
            wasteManagement = new wasteManagement.WasteManagement("station1", cg.getGraph());
        } catch (IOException | SAXException | ParserConfigurationException e1) {
            e1.printStackTrace();
        }
    }

    private void addListeners() {
        analysisButton.addActionListener((ActionEvent e) -> {
            assert wasteManagement != null;
            ArrayList<String> generalInfo = wasteManagement.getGeneralInfo();
            ArrayList<String> trucksInfo = wasteManagement.getTrucksInfo();
            ArrayList<String> residueInfo = wasteManagement.getResidueInfo();
            ArrayList<ArrayList<Double>> containersInfo = wasteManagement.getContainersInfo();
            Statistics s = this.parent.getStatistics();
            s.setGeneralInfo(generalInfo);
            s.setTrucksInfo(trucksInfo);
            s.setResidueInfo(residueInfo);
            s.setNodesInfo(containersInfo);
            this.parent.showLayout("Statistics");
        });

        endShiftButton.addActionListener((ActionEvent e) -> {
            wasteManagement.emptyTrucks();
            wasteManagement.refillContainers();
        });

        wasteCollectionButton.addActionListener((ActionEvent e) -> {

        });
    }

    public JPanel getPane() {
        return pane;
    }

    public void setPane(JPanel pane) {
        this.pane = pane;
    }

    public void setVisible(boolean b){
        this.pane.setVisible(b);
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void setBackButton(JButton backButton) {
        this.backButton = backButton;
    }

    public WasteManagement getWasteManagement(){
        return wasteManagement;
    }
}
