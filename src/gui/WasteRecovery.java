package gui;

import javax.swing.*;

public class WasteRecovery {
    private JPanel pane;
    private JButton backButton;
    private JList Astar;
    private JList dfs;
    private JList bfs;
    private MainWindow parent;

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
}