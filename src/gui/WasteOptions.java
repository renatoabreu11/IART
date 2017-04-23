package gui;

import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

public class WasteOptions{
    private JPanel pane;
    private JButton backButton;
    private JComboBox<String> graphSelection;
    private JComboBox<String> stationSelection;

    public WasteOptions() {
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
