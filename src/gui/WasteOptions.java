package gui;

import wasteManagement.Truck;

import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class WasteOptions{
    private JPanel pane;
    private JSpinner betaValue;
    private JSpinner alfaValue;
    private JComboBox<String> graphSelection;
    private JComboBox<String> stationSelection;
    private JComboBox<String> wasteCollection;
    private JButton backButton;
    private JList<String> truckSelection;

    public WasteOptions(Management management) {
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

        wasteCollection.addItem("Default");
        wasteCollection.addItem("Household");
        wasteCollection.addItem("Paper");
        wasteCollection.addItem("Glass");
        wasteCollection.addItem("Plastic");

        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Truck> trucks = management.getWasteManagement().getTrucks();
        for(Truck t : trucks){
            listModel.addElement(t.getResidue().toString() + " truck; Maximum capacity: " + t.getMaxCapacity() + "kg");
        }
        truckSelection.setModel(listModel);

        SpinnerModel model =
                new SpinnerNumberModel(0.5, //initial value
                        0, //min
                        1, //max
                        0.1);
        alfaValue.setModel(model);
        betaValue.setModel(model);

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

    public JComboBox<String> getGraphSelection() {
        return graphSelection;
    }

    public void setGraphSelection(JComboBox<String> graphSelection) {
        this.graphSelection = graphSelection;
    }

    public JComboBox<String> getStationSelection() {
        return stationSelection;
    }

    public void setStationSelection(JComboBox<String> stationSelection) {
        this.stationSelection = stationSelection;
    }
}
