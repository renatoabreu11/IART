package gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame{
    private MainOptions mainOptions;
    private WasteOptions wasteOptions;
    private WasteManagement wasteManagement;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainWindow frame = new MainWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainWindow() {
        setTitle("Waste Management");
        setSize(512, 512);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel(new CardLayout());
        setContentPane(contentPane);
        initialize();
        setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        mainOptions = new MainOptions();
        mainOptions.setVisible(true);

        wasteOptions = new WasteOptions();
        wasteOptions.setVisible(true);

        wasteManagement = new WasteManagement(this);
        wasteManagement.setVisible(true);

        contentPane.add(mainOptions.getPane(), "Main Options");
        contentPane.add(wasteOptions.getPane(), "Waste Options");
        contentPane.add(wasteManagement.getPane(), "Waste Management");

        addListeners();

        CardLayout cardLayout = (CardLayout) contentPane.getLayout();
        cardLayout.show(contentPane, "Main Options");
    }

    private void addListeners(){
        mainOptions.getOptionsButton().addActionListener(e ->{
            CardLayout cardLayout = (CardLayout) contentPane.getLayout();
            cardLayout.show(contentPane, "Waste Options");
        });

        mainOptions.getWasteRecoveryButton().addActionListener(e ->{
            CardLayout cardLayout = (CardLayout) contentPane.getLayout();
            cardLayout.show(contentPane, "Waste Management");
        });

        wasteOptions.getBackButton().addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) contentPane.getLayout();
            cardLayout.show(contentPane, "Main Options");
        });

        wasteManagement.getBackButton().addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) contentPane.getLayout();
            cardLayout.show(contentPane, "Main Options");
        });
    }
}
