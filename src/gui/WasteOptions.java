package gui;

import org.xml.sax.SAXException;
import wasteManagement.CityGraph;
import wasteManagement.Truck;
import wasteManagement.WasteManagement;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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
    private MainWindow parent;

    public WasteOptions(MainWindow mainWindow) {
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

        wasteCollection.addItem("Default");
        wasteCollection.addItem("Household");
        wasteCollection.addItem("Paper");
        wasteCollection.addItem("Glass");
        wasteCollection.addItem("Plastic");

        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Truck> trucks = this.parent.getWasteManagement().getTrucks();
        ArrayList<Integer> indexes = new ArrayList<>();
        int i = 0;
        for(Truck t : trucks){
            listModel.addElement(t.getResidue().toString() + " truck; Maximum capacity: " + t.getMaxCapacity() + "kg");
            indexes.add(i);
            i++;
        }
        truckSelection.setModel(listModel);

        int[] intIndex = new int[indexes.size()];
        for(i = 0; i < indexes.size(); i++){
            intIndex[i] = indexes.get(i);
        }

        truckSelection.setSelectedIndices(intIndex);

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
        stationSelection.addActionListener(actionEvent -> {
            String stationFile = (String) stationSelection.getSelectedItem();
            WasteManagement wasteManagement = parent.getWasteManagement();
            String curr_station = wasteManagement.getStationFile();
            if(!stationFile.equals(curr_station)){
                try {
                    parent.setWasteManagement(new WasteManagement(stationFile, parent.getCityGraph().getGraph()));
                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }

                DefaultListModel<String> listModel = new DefaultListModel<>();
                ArrayList<Truck> trucks = this.parent.getWasteManagement().getTrucks();
                ArrayList<Integer> indexes = new ArrayList<>();
                int i = 0;
                for(Truck t : trucks){
                    listModel.addElement(t.getResidue().toString() + " truck; Maximum capacity: " + t.getMaxCapacity() + "kg");
                    indexes.add(i);
                    i++;
                }
                truckSelection.setModel(listModel);

                int[] intIndex = new int[indexes.size()];
                for(i = 0; i < indexes.size(); i++){
                    intIndex[i] = indexes.get(i);
                }

                truckSelection.setSelectedIndices(intIndex);
            }
        });

        graphSelection.addActionListener(actionEvent -> {
            String graphFile = (String) graphSelection.getSelectedItem();
            String curr_graph = parent.getCityGraph().getGraphFile();
            if(!graphFile.equals(curr_graph)){
                this.parent.setCityGraph(new CityGraph(graphFile));
                this.parent.updateWasteManagement();
            }
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
