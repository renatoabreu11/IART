package gui;

import org.xml.sax.SAXException;
import wasteManagement.CityGraph;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class WasteManagement{
    private JFrame parent;
    private JPanel pane;
    private JButton backButton;
    private JButton startAnalysisButton;

    public WasteManagement(MainWindow mainWindow) {
        this.parent = mainWindow;
        addListeners();
    }

    private void addListeners() {
        startAnalysisButton.addActionListener(e -> {
            try {
                startAnalisys();
            } catch (ParserConfigurationException |SAXException |IOException error) {
                error.printStackTrace();
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

    public static void startAnalisys() throws ParserConfigurationException, SAXException, IOException {

        //String graphFileName = (String)graphSelection.getSelectedItem();
        //String stationFileName = (String)stationSelection.getSelectedItem();
        CityGraph cg = new CityGraph("graph1");

        wasteManagement.WasteManagement management = new wasteManagement.WasteManagement("station1", cg.getGraph());
        management.printManagementDetails();
        System.out.println();
        /*
        final DefaultGraph g = new DefaultGraph("my beautiful graph");
        g.setStrict(false);
        Viewer viewer = new Viewer(g, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        frame.setPreferredSize(new Dimension(600, 600));
        DefaultView view = (DefaultView) viewer.addDefaultView(false);   // false indicates "no JFrame".
        view.setPreferredSize(new Dimension(400, 400));
        frame.setLayout(new FlowLayout());
        frame.add(view);
        JButton myButton = new JButton("MyButton");
        myButton.addActionListener(e -> System.out.println("Somebody pushed my button."));
        frame.add(myButton);
        JSlider slider = new JSlider();
        slider.addChangeListener(e -> view.getCamera().setViewPercent(slider.getValue() / 10.0));
        frame.add(slider);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewer.enableAutoLayout();
        ViewerPipe vp = viewer.newViewerPipe();
        vp.addViewerListener(new ViewerListener() {
            @Override
            public void viewClosed(String viewName) {
                // dont care
            }

            @Override
            public void buttonPushed(String id) {
                Node n = g.getNode(id);
                String attributes[] = n.getAttributeKeySet().toArray(new String[n.getAttributeKeySet().size()]);

                String attributeToChange = (String) JOptionPane.showInputDialog(null, "Select attibute to modify", "Attribute for " + id, JOptionPane.QUESTION_MESSAGE, null, attributes, attributes[0]);
                String curValue = n.getAttribute(attributeToChange);
                String newValue
                        = JOptionPane.showInputDialog("New Value", curValue);
                n.setAttribute(attributeToChange, newValue);
            }

            @Override
            public void buttonReleased(String id) {
                // don't care
            }
        });
        g.addNode("A");
        g.addNode("B");
        g.addNode("C");

        g.addNode("E");
        g.addNode("F");
        g.addNode("G");

        g.addNode("1");
        g.addNode("2");
        g.addNode("3");

        g.addNode("4");
        g.addNode("5");
        g.addNode("6");

        g.addEdge("AB", "A", "B");
        g.addEdge("AC", "B", "C");
        g.addEdge("BC", "C", "C");

        g.addEdge("EB", "E", "B");
        g.addEdge("FC", "F", "C");
        g.addEdge("GC", "G", "C");

        g.addEdge("1B", "1", "B");
        g.addEdge("2C", "2", "C");
        g.addEdge("3C", "3", "C");

        g.addEdge("4B", "4", "B");
        g.addEdge("5C", "5", "C");
        g.addEdge("6C", "6", "C");

        g.getNode("A").setAttribute("size", "big");
        g.getNode("B").setAttribute("size", "medium");
        g.getNode("C").setAttribute("size", "small");
        g.getNode("A").setAttribute("ui:color", "red");
        g.getNode("B").setAttribute("ui:color", "blue");
        g.getNode("C").setAttribute("ui:color", "green");

        for (Node node : g) {
            node.addAttribute("ui.label", node.getId());
        }
        while (true) {
            (view).repaint();
            vp.pump();
        }*/
    }
}
