package gui;

import org.xml.sax.SAXException;
import wasteManagement.WasteManagement;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public class WasteOptions implements ChangeListener {
    private JPanel pane;
    private JSpinner betaValue;
    private JSpinner alfaValue;
    private JComboBox<String> graphSelection;
    private JComboBox<String> stationSelection;
    private JComboBox<String> wasteCollection;
    private JButton backButton;
    private JSpinner maxTrucks;
    private MainWindow parent;

    WasteOptions(MainWindow mainWindow) {
        this.parent = mainWindow;
        File dirData = new File("data");
        FilenameFilter filter = (file, s) -> Pattern.matches(".+\\.dgs", s);
        File[] files = dirData.listFiles(filter);
        assert files != null;
        for (File file: files)
            graphSelection.addItem(file.getName());

        filter = (file, s) -> Pattern.matches(".+\\.xml", s);
        files = dirData.listFiles(filter);
        assert files != null;
        for (File file: files)
            stationSelection.addItem(file.getName());

        String graph = (String) graphSelection.getSelectedItem();
        String station = (String) stationSelection.getSelectedItem();
        this.parent.initWasteManagement(graph, station);

        wasteCollection.addItem("Household");
        wasteCollection.addItem("Paper");
        wasteCollection.addItem("Glass");
        wasteCollection.addItem("Plastic");

        SpinnerModel model =
                new SpinnerNumberModel(0.5, //initial value
                        0, //min
                        1, //max
                        0.1);

        SpinnerModel model1 =
                new SpinnerNumberModel(0.5, //initial value
                        0, //min
                        1, //max
                        0.1);

        SpinnerModel model2 =
                new SpinnerNumberModel(5, //initial value
                        1, //min
                        15, //max
                        1);
        maxTrucks.setModel(model2);
        alfaValue.setModel(model);
        betaValue.setModel(model1);
        alfaValue.addChangeListener(this);
        betaValue.addChangeListener(this);
        addListeners();
    }

    private void addListeners() {
        stationSelection.addActionListener(actionEvent -> {
            String stationFile = (String) stationSelection.getSelectedItem();
            WasteManagement wasteManagement = parent.getWasteManagement();
            String curr_station = wasteManagement.getStationFile();
            if(!stationFile.equals(curr_station)){
                try {
                    parent.setWasteManagement(new WasteManagement(stationFile, parent.getGraph()));
                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        });

        graphSelection.addActionListener(actionEvent -> {
            String graphFile = (String) graphSelection.getSelectedItem();
            String curr_graph = parent.getGraphFile();
            if(!graphFile.equals(curr_graph)){
                this.parent.setGraph(graphFile);
                this.parent.updateWasteManagement();
                this.parent.updateGraphPanel();
            }
        });

    }

    public JPanel getPane() {
        return pane;
    }

    void setVisible(boolean b){
        this.pane.setVisible(b);
    }

    JButton getBackButton() {
        return backButton;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSpinner spinner = (JSpinner) e.getSource();
        double value = (double)spinner.getValue();
        if(spinner == alfaValue){
            double oldValue = (double)betaValue.getValue();
            double diff = round(1 - (value + oldValue));
            double newValue = round(oldValue + diff);
            betaValue.setValue(newValue);
        }else{
            double oldValue = (double)alfaValue.getValue();
            double diff = round(1 - (value + oldValue));
            double newValue = round(oldValue + diff);
            alfaValue.setValue(newValue);
        }
    }

    private static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    JSpinner getBetaValue() {
        return betaValue;
    }

    JSpinner getAlfaValue() {
        return alfaValue;
    }

    JComboBox<String> getWasteCollection() {
        return wasteCollection;
    }

    JSpinner getMaxTrucks() {
        return maxTrucks;
    }
}
