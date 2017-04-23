package gui;

import org.xml.sax.SAXException;
import wasteManagement.CityGraph;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

public class WasteManagement{
    private MainWindow parent;
    private JPanel pane;
    private JButton backButton;
    private JButton startAnalysisButton;

    public WasteManagement(MainWindow mainWindow) {
        this.parent = mainWindow;
        addListeners();
    }

    private void addListeners() {
        startAnalysisButton.addActionListener((ActionEvent e) -> {
            CityGraph cg = new CityGraph("graph1");

            wasteManagement.WasteManagement management = null;
            try {
                management = new wasteManagement.WasteManagement("station1", cg.getGraph());
            } catch (IOException | SAXException | ParserConfigurationException e1) {
                e1.printStackTrace();
            }

            assert management != null;
            ArrayList<String> generalInfo = management.getGeneralInfo();
            ArrayList<String> trucksInfo = management.getTrucksInfo();
            Statistics s = this.parent.getStatistics();
            s.setGeneralInfo(generalInfo);
            s.setTrucksInfo(trucksInfo);
            this.parent.showLayout("Statistics");
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
}
