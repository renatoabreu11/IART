package gui;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class Statistics implements TableModelListener {
    private JTextArea info;
    private JTextArea trucks;
    private JButton backButton;
    private JTable nodes;
    private JPanel pane;
    private JTextArea residueBuildup;
    private Management parent;

    public Statistics(Management m) {
        this.parent = m;
        addListeners();
        nodes.setAutoCreateRowSorter(true);
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

    public void setResidueInfo(ArrayList<String> info){
        this.residueBuildup.setText("");
        for(String i : info){
            this.residueBuildup.append(i + "\n");
        }
    }

    public void setNodesInfo(ArrayList<ArrayList<Double>> info){
        DefaultTableModel model = new DefaultTableModel() {

            boolean[] canEdit = new boolean[]{
                    false, true, true, true, true
            };

            public Class<?> getColumnClass(int columnIndex){
                return Double.class;
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        model.addColumn("Location");
        model.addColumn("Household");
        model.addColumn("Paper");
        model.addColumn("Plastic");
        model.addColumn("Glass");

        for (ArrayList<Double> nodeInfo : info) {
            Object[] o = nodeInfo.toArray();
            model.addRow(o);
        }
        model.addTableModelListener(this);
        nodes.setModel(model);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        double newValue = (Double) model.getValueAt(row, column);
        Object location = model.getValueAt(row, 0);
        this.parent.getWasteManagement().updateContainer(String.valueOf(location).split("\\.")[0], newValue, columnName);
        ArrayList<String> residueInfo = this.parent.getWasteManagement().getResidueInfo();
        setResidueInfo(residueInfo);
    }
}
