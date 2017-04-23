package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Created by helder on 23-04-2017.
 */
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

        JButton startAnalisys = new JButton("start analisys");
        startAnalisys.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

            }
        });
        startAnalisys.setBounds(110, 300, 180, 30);
        frame.getContentPane().add(startAnalisys);

        JComboBox graphSelection = new JComboBox();
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

        JLabel graphLabel = new JLabel();
        graphLabel.setText("Graph file: ");
        graphLabel.setLabelFor(graphSelection);
        graphLabel.setBounds(50, 180, 200, 25);
        frame.add(graphLabel);

        JComboBox stationSelection = new JComboBox();
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

        JLabel stationLabel = new JLabel();
        stationLabel.setText("Station file: ");
        stationLabel.setLabelFor(graphSelection);
        stationLabel.setBounds(50, 220, 200, 25);
        stationLabel.setLabelFor(stationSelection);
        frame.add(stationLabel);

        JLabel logo = new JLabel(new ImageIcon("data/logo.png"));
        logo.setBounds(110, 50, 180, 71);
        frame.add(logo);

        JLabel welcomeMsg = new JLabel("welcomeMsg");
        welcomeMsg.setText("Welcome to waste management.");
        welcomeMsg.setBounds(0, 130, 400, 20);
        welcomeMsg.setHorizontalAlignment(JLabel.CENTER);
        frame.add(welcomeMsg);



    }

    public static void main(String[] args) {
        new App();
    }

}
