package gui;

import wasteManagement.WasteManagement;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Management {
    private MainWindow parent;
    private JPanel pane;
    private JButton backButton;
    private JButton analysisButton;
    private JButton endShiftButton;
    private JButton wasteCollectionButton;

    public Management(MainWindow mainWindow) {
        this.parent = mainWindow;
        addListeners();
    }

    private void addListeners() {
        analysisButton.addActionListener((ActionEvent e) -> {
            ArrayList<String> generalInfo = parent.getWasteManagement().getGeneralInfo();
            ArrayList<String> trucksInfo = parent.getWasteManagement().getTrucksInfo();
            ArrayList<String> residueInfo = parent.getWasteManagement().getResidueInfo();
            ArrayList<ArrayList<Double>> containersInfo = parent.getWasteManagement().getContainersInfo();
            Statistics s = this.parent.getStatistics();
            s.setGeneralInfo(generalInfo);
            s.setTrucksInfo(trucksInfo);
            s.setResidueInfo(residueInfo);
            s.setNodesInfo(containersInfo);
            this.parent.showLayout("Statistics");
        });

        endShiftButton.addActionListener((ActionEvent e) -> {
            parent.getWasteManagement().emptyTrucks();
            parent.getWasteManagement().refillContainers();
        });

        wasteCollectionButton.addActionListener((ActionEvent e) -> this.parent.showLayout("Waste Recovery"));
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
}
