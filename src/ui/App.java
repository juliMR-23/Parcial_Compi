package ui;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    public App() {
        setTitle("The Feline Graph Chronicles");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setBackground(new Color(10, 10, 26));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(new LandingPage(this), "landing");
        cardPanel.add(new Mission1Panel(this), "mission1");
        cardPanel.add(new Mission2Panel(this), "mission2");
        cardPanel.add(new Mission3Panel(this), "mission3");
        cardPanel.add(new Mission4Panel(this), "mission4");

        setContentPane(cardPanel);
        setVisible(true);
    }

    public void showMission(String name) {
        cardLayout.show(cardPanel, name);
    }
}
