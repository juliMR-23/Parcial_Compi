package ui;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {

    public App() {
        setTitle("The Feline Graph Chronicles");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
        setBackground(new Color(10, 10, 26));
        setContentPane(new LandingPage());
        setVisible(true);
    }
}
