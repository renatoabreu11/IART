package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainOptions{
    private JPanel pane;
    private BufferedImage background;
    private JButton wasteRecoveryButton;
    private JButton exitButton;
    private JButton optionsButton;

    public MainOptions() {
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

    public void setPane(JPanel pane) {
        this.pane = pane;
    }

    public BufferedImage getBackground() {
        return background;
    }

    public void setBackground(BufferedImage background) {
        this.background = background;
    }

    public JButton getWasteRecoveryButton() {
        return wasteRecoveryButton;
    }

    public void setWasteRecoveryButton(JButton wasteRecoveryButton) {
        this.wasteRecoveryButton = wasteRecoveryButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    public void setExitButton(JButton exitButton) {
        this.exitButton = exitButton;
    }

    public JButton getOptionsButton() {
        return optionsButton;
    }

    public void setOptionsButton(JButton optionsButton) {
        this.optionsButton = optionsButton;
    }

    public void setVisible(boolean b){
        this.pane.setVisible(b);
    }

}
