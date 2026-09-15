package ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

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

        JLabel subtitle = new JLabel("Pola  |  Minerva  |  Nina  |  Limón  |  Nero");
        subtitle.setForeground(NEON_MAGENTA);
        subtitle.setFont(new Font("Consolas", Font.PLAIN, 12));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel tagline = new JLabel("Decodifica los grafos. Rescata a Nina. Derrota a Limón.");
        tagline.setForeground(TEXT_DIM);
        tagline.setFont(new Font("Consolas", Font.ITALIC, 11));
        tagline.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(BG);
        header.setBorder(BorderFactory.createEmptyBorder(30, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 4, 0);

        gbc.gridy = 0;
        header.add(title, gbc);
        gbc.gridy = 1;
        header.add(subtitle, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(6, 0, 0, 0);
        header.add(tagline, gbc);
        return header;
    }

    private JPanel createContent() {
        JPanel grid = new JPanel(new GridLayout(1, 4, 16, 0));
        grid.setBackground(BG);
        grid.setBorder(BorderFactory.createEmptyBorder(16, 40, 24, 40));

        grid.add(createMissionCard("MISIÓN 1", "BFS & DFS", NEON_AMBER,
            "Navega un campo de minas R×C: halla la distancia mínima (BFS) y el orden de exploración (DFS) desde el inicio hasta Nina",
            "mission1"));
        grid.add(createMissionCard("MISIÓN 2", "Dijkstra", NEON_VIOLET,
            "Grafo ponderado no dirigido: calcula el camino de menor costo entre el origen y el destino con pesos no negativos",
            "mission2"));
        grid.add(createMissionCard("MISIÓN 3", "Floyd-Warshall & Bellman-Ford", NEON_PINK,
            "Maximiza el churun recolectado en un grafo dirigido. Detecta ciclos de ganancia positiva → «Infinite churun!»",
            "mission3"));
        grid.add(createMissionCard("MISIÓN 4", "Kruskal", NEON_GREEN,
            "Reconecta la red destruida con el costo mínimo posible (MST). Si no se puede conectar todo → «Limón cut too many cables»",
            "mission4"));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG);
        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    private static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

        RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Double(x + thickness / 2.0, y + thickness / 2.0,
                    w - thickness, h - thickness, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            int pad = thickness + 4;
            return new Insets(pad, pad, pad, pad);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    private JPanel createMissionCard(String mission, String algorithm,
                                      Color neonColor, String description, String cardName) {
        int radius = 16;
        int pad = 20;

        JLabel missionLabel = new JLabel(mission, SwingConstants.CENTER);
        missionLabel.setForeground(neonColor);
        missionLabel.setFont(new Font("Consolas", Font.BOLD, 10));

        JLabel algoLabel = new JLabel("<html><div style='text-align:center;'>" + algorithm + "</div></html>", SwingConstants.CENTER);
        algoLabel.setForeground(Color.WHITE);
        algoLabel.setFont(new Font("Consolas", Font.BOLD, 15));

        JLabel descLabel = new JLabel("<html><div style='text-align:center; word-wrap:break-word;'>" + description + "</div></html>", SwingConstants.CENTER);
        descLabel.setForeground(TEXT_DIM);
        descLabel.setFont(new Font("Consolas", Font.PLAIN, 10));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(pad - 4, pad, pad, pad));
        textPanel.add(center(missionLabel));
        textPanel.add(Box.createVerticalStrut(12));
        textPanel.add(center(algoLabel));
        textPanel.add(Box.createVerticalStrut(20));
        textPanel.add(center(descLabel));

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBackground(BG_CARD);
        card.setBorder(new RoundedBorder(neonColor, 1, radius));
        card.setMinimumSize(new Dimension(160, 220));
        card.setMaximumSize(new Dimension(300, 300));
        card.add(textPanel, BorderLayout.CENTER);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(new RoundedBorder(neonColor, 2, radius));
                card.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(new RoundedBorder(neonColor, 1, radius));
                card.repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                app.showMission(cardName);
            }
        });

        return card;
    }

    private static JPanel center(Component comp) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        wrapper.add(comp, gbc);
        return wrapper;
    }

    private JLabel createFooter() {
        JLabel footer = new JLabel("The Feline Graph Chronicles — Lenguajes y Compiladores");
        footer.setForeground(new Color(50, 50, 80));
        footer.setFont(new Font("Consolas", Font.PLAIN, 10));
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        return footer;
    }
}
