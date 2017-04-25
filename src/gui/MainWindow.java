package gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame{
    private MainOptions mainOptions;
    private WasteOptions wasteOptions;
    private Management management;
    private Statistics statistics;
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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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

        management = new Management(this);
        management.setVisible(true);

        wasteOptions = new WasteOptions(management);
        wasteOptions.setVisible(true);

        statistics = new Statistics(management);
        statistics.setVisible(true);

        contentPane.add(mainOptions.getPane(), "Main Options");
        contentPane.add(wasteOptions.getPane(), "Waste Options");
        contentPane.add(management.getPane(), "Waste Management");
        contentPane.add(statistics.getPane(), "Statistics");

        addListeners();

        CardLayout cardLayout = (CardLayout) contentPane.getLayout();
        cardLayout.show(contentPane, "Main Options");
    }

    private void addListeners(){
        mainOptions.getOptionsButton().addActionListener(e -> showLayout("Waste Options"));

        mainOptions.getWasteRecoveryButton().addActionListener(e -> showLayout("Waste Management"));

        wasteOptions.getBackButton().addActionListener(e -> showLayout("Main Options"));

        management.getBackButton().addActionListener(e -> showLayout("Main Options"));

        statistics.getBackButton().addActionListener(e -> showLayout("Waste Management"));
    }

    public void showLayout(String layout){
        CardLayout cardLayout = (CardLayout) contentPane.getLayout();
        cardLayout.show(contentPane, layout);
    }

    public Statistics getStatistics(){
        return statistics;
    }
}
