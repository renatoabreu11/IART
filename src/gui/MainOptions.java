package gui;

import javax.swing.*;
import java.awt.*;

public class MainOptions{
    private JPanel pane;
    private JButton wasteRecoveryButton;
    private JButton exitButton;
    private JButton optionsButton;

    MainOptions() {
        addListeners();
    }

    private void addListeners() {
        exitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", null,
                    JOptionPane.YES_NO_OPTION);

            if(result == JOptionPane.YES_OPTION){
                Container frame = exitButton.getParent();
                do
                    frame = frame.getParent();
                while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
                System.exit(0);
            }
        });
    }

    public JPanel getPane() {
        return pane;
    }

    JButton getWasteRecoveryButton() {
        return wasteRecoveryButton;
    }

    JButton getOptionsButton() {
        return optionsButton;
    }

    void setVisible(){
        this.pane.setVisible(true);
    }

}
