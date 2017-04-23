package gui;

import javax.swing.*;

public class WasteManagement{
    private JPanel pane;
    private JButton backButton;

    public WasteManagement() {
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
