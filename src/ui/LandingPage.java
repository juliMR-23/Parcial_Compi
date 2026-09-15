package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LandingPage extends JPanel {

    private static final Color BG = new Color(10, 10, 26);
    private static final Color BG_CARD = new Color(15, 15, 40);
    private static final Color NEON_CYAN = new Color(0, 255, 245);
    private static final Color NEON_MAGENTA = new Color(255, 0, 255);
    private static final Color NEON_PINK = new Color(255, 42, 109);
    private static final Color NEON_GREEN = new Color(5, 255, 161);
    private static final Color NEON_AMBER = new Color(255, 149, 0);
    private static final Color NEON_VIOLET = new Color(191, 0, 255);
    private static final Color TEXT_DIM = new Color(120, 120, 160);

    private final App app;

    public LandingPage(App app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(BG);
        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JLabel title = new JLabel("THE FELINE GRAPH CHRONICLES");
        title.setForeground(NEON_CYAN);
        title.setFont(new Font("Consolas", Font.BOLD, 28));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(35, 0, 5, 0));

        JLabel subtitle = new JLabel("\u25C6  Pola  \u25C6  Minerva  \u25C6  Nina  \u25C6  Lim\u00f3n  \u25C6");
        subtitle.setForeground(NEON_MAGENTA);
        subtitle.setFont(new Font("Consolas", Font.PLAIN, 12));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel tagline = new JLabel("Decodifica los grafos. Rescata a Nina. Derrota a Lim\u00f3n.");
        tagline.setForeground(TEXT_DIM);
        tagline.setFont(new Font("Consolas", Font.ITALIC, 11));
        tagline.setHorizontalAlignment(SwingConstants.CENTER);
        tagline.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG);
        header.add(title);
        header.add(subtitle);
        header.add(tagline);
        return header;
    }

    private JPanel createContent() {
        JPanel grid = new JPanel(new GridLayout(1, 4, 20, 0));
        grid.setBackground(BG);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));

        grid.add(createMissionCard("MISI\u00d3N 1", "Pola", "BFS & DFS", NEON_AMBER,
            "Rescata Nina del campo de minas", "mission1"));
        grid.add(createMissionCard("MISI\u00d3N 2", "Minerva", "Dijkstra", NEON_VIOLET,
            "Recupera las cuentas de Claude", "mission2"));
        grid.add(createMissionCard("MISI\u00d3N 3", "Nina", "Floyd-Warshall & Bellman-Ford", NEON_PINK,
            "El almac\u00e9n definitivo de churun", "mission3"));
        grid.add(createMissionCard("MISI\u00d3N 4", "Lim\u00f3n", "Kruskal", NEON_GREEN,
            "Reconecta la red destruida", "mission4"));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG);
        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createMissionCard(String mission, String hero, String algorithm,
                                      Color neonColor, String description, String cardName) {
        JLabel missionLabel = new JLabel(mission);
        missionLabel.setForeground(neonColor);
        missionLabel.setFont(new Font("Consolas", Font.BOLD, 11));

        JLabel heroLabel = new JLabel(hero);
        heroLabel.setForeground(Color.WHITE);
        heroLabel.setFont(new Font("Consolas", Font.BOLD, 18));

        JLabel algoLabel = new JLabel("<html><i>" + algorithm + "</i></html>");
        algoLabel.setForeground(neonColor.darker());
        algoLabel.setFont(new Font("Consolas", Font.PLAIN, 11));

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setForeground(TEXT_DIM);
        descLabel.setFont(new Font("Consolas", Font.PLAIN, 10));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(BG_CARD);
        textPanel.add(missionLabel);
        textPanel.add(Box.createVerticalStrut(6));
        textPanel.add(heroLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(algoLabel);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(descLabel);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(neonColor, 1),
            BorderFactory.createEmptyBorder(20, 18, 20, 18)
        ));
        card.setPreferredSize(new Dimension(200, 220));
        card.add(textPanel, BorderLayout.CENTER);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new MouseAdapter() {
            private Color currentColor = neonColor;
            @Override
            public void mouseEntered(MouseEvent e) {
                currentColor = neonColor;
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(neonColor, 2),
                    BorderFactory.createEmptyBorder(19, 17, 19, 17)
                ));
                card.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(neonColor, 1),
                    BorderFactory.createEmptyBorder(20, 18, 20, 18)
                ));
                card.repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                app.showMission(cardName);
            }
        });

        return card;
    }

    private JLabel createFooter() {
        JLabel footer = new JLabel("The Feline Graph Chronicles \u2014 Lenguajes y Compiladores");
        footer.setForeground(new Color(50, 50, 80));
        footer.setFont(new Font("Consolas", Font.PLAIN, 10));
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        return footer;
    }
}
