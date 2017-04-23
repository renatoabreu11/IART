package gui;

import org.xml.sax.SAXException;
import wasteManagement.CityGraph;
import wasteManagement.WasteManagement;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Pattern;

public class App {

    private JFrame frame;
    private JPanel mainPanel;

    public App() {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {

        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        final JLabel logo = new JLabel(new ImageIcon("data/logo.png"));
        logo.setBounds(110, 50, 180, 71);
        frame.add(logo);

        final JLabel welcomeMsg = new JLabel("welcomeMsg");
        welcomeMsg.setText("Welcome to waste management.");
        welcomeMsg.setBounds(0, 130, 400, 20);
        welcomeMsg.setHorizontalAlignment(JLabel.CENTER);
        frame.add(welcomeMsg);

        final JComboBox graphSelection = new JComboBox();
        File dirData = new File("data");
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return Pattern.matches(".+\\.dgs", s);
            }
        };
        File[] files = dirData.listFiles(filter);
        for (File file: files)
            graphSelection.addItem(file.getName());
        graphSelection.setBounds(200, 180, 125, 25);
        frame.getContentPane().add(graphSelection);

        final JLabel graphLabel = new JLabel();
        graphLabel.setText("Graph file: ");
        graphLabel.setLabelFor(graphSelection);
        graphLabel.setBounds(50, 180, 200, 25);
        frame.add(graphLabel);

        final JComboBox stationSelection = new JComboBox();
        filter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return Pattern.matches(".+\\.xml", s);
            }
        };
        files = dirData.listFiles(filter);
        for (File file: files)
            stationSelection.addItem(file.getName());
        stationSelection.setBounds(200, 220, 125, 25);
        frame.getContentPane().add(stationSelection);

        final JLabel stationLabel = new JLabel();
        stationLabel.setText("Station file: ");
        stationLabel.setLabelFor(graphSelection);
        stationLabel.setBounds(50, 220, 200, 25);
        stationLabel.setLabelFor(stationSelection);
        frame.add(stationLabel);

        final JButton startAnalisys = new JButton("start analisys");
        startAnalisys.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                logo.setVisible(false);
                welcomeMsg.setVisible(false);
                graphSelection.setVisible(false);
                graphLabel.setVisible(false);
                stationSelection.setVisible(false);
                stationLabel.setVisible(false);
                startAnalisys.setVisible(false);
                String graphFileName = (String)graphSelection.getSelectedItem();
                String stationFileName = (String)stationSelection.getSelectedItem();
                try {
                    startAnalisys(frame, graphFileName, stationFileName);
                } catch (ParserConfigurationException|SAXException|IOException e) {
                    e.printStackTrace();
                }

            }
        });
        startAnalisys.setBounds(110, 300, 180, 30);
        frame.getContentPane().add(startAnalisys);

    }

    public static void main(String[] args) {
        new App();
    }

    public static void startAnalisys(JFrame frame, String graphFileName, String stationFileName) throws ParserConfigurationException, SAXException, IOException {

        CityGraph cg = new CityGraph("graph1");

        WasteManagement management = new WasteManagement("station1", cg.getGraph());
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
