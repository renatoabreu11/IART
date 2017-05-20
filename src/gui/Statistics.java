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
    private MainWindow parent;

    Statistics(MainWindow mainWindow) {
        this.parent = mainWindow;
        addListeners();
        nodes.setAutoCreateRowSorter(true);
    }

    JButton getBackButton() {
        return backButton;
    }

    public JPanel getPane() {
        return pane;
    }

    private void addListeners() {
    }

    void setVisible(){
        this.pane.setVisible(true);
    }

    void setGeneralInfo(ArrayList<String> info){
        this.info.setText("");
        for(String i : info){
            this.info.append(i + "\n");
        }
    }

    void setTrucksInfo(ArrayList<String> info){
        this.trucks.setText("");
        for(String i : info){
            this.trucks.append(i + "\n");
        }
    }

    void setResidueInfo(ArrayList<String> info){
        this.residueBuildup.setText("");
        for(String i : info){
            this.residueBuildup.append(i + "\n");
        }
    }

    void setNodesInfo(ArrayList<ArrayList<Double>> info){
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
