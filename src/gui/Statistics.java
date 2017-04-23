package gui;

import javax.swing.*;
import java.util.ArrayList;

public class Statistics {
    private JTextArea info;
    private JTextArea trucks;
    private JButton backButton;
    private JTable nodes;
    private JPanel pane;

    public Statistics() {
        addListeners();
    }

    public JTextArea getInfo() {
        return info;
    }

    public void setInfo(JTextArea info) {
        this.info = info;
    }

    public JTextArea getTrucks() {
        return trucks;
    }

    public void setTrucks(JTextArea trucks) {
        this.trucks = trucks;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void setBackButton(JButton backButton) {
        this.backButton = backButton;
    }

    public JTable getNodes() {
        return nodes;
    }

    public void setNodes(JTable nodes) {
        this.nodes = nodes;
    }

    public JPanel getPane() {
        return pane;
    }

    public void setPane(JPanel pane) {
        this.pane = pane;
    }

    private void addListeners() {
    }

    public void setVisible(boolean b){
        this.pane.setVisible(b);
    }

    public void setGeneralInfo(ArrayList<String> info){
        this.info.setText("");
        for(String i : info){
            this.info.append(i + "\n");
        }
    }

    public void setTrucksInfo(ArrayList<String> info){
        this.trucks.setText("");
        for(String i : info){
            this.trucks.append(i + "\n");
        }
    }
}
