package ui;

import algorithms.FloydWarshallSolver;

import javax.swing.*;
import java.awt.*;

public class FloydMatrixCanvas extends JPanel {

    private static final Color BG = new Color(10, 10, 26);
    private static final Color HEADER_BG = new Color(20, 20, 50);
    private static final Color HEADER_FG = new Color(0, 255, 245);
    private static final Color CELL_FG = new Color(200, 200, 220);
    private static final Color UNBounded_FG = new Color(255, 149, 0);
    private static final Color NO_ROUTE_FG = new Color(80, 85, 120);
    private static final Color GRID_LINE = new Color(40, 45, 80);
    private static final Color HIGHLIGHT_BG = new Color(0, 255, 245, 30);

    private long[][] distances;
    private boolean[][] unbounded;
    private int N;
    private int highlightR = -1, highlightC = -1;

    public FloydMatrixCanvas() {
        setBackground(BG);
    }

    public void setMatrix(FloydWarshallSolver solver, long[][] dist, int N) {
        this.distances = dist;
        this.N = N;
        this.unbounded = new boolean[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                this.unbounded[i][j] = solver.isUnbounded(i, j);
        this.highlightR = -1;
        this.highlightC = -1;
        repaint();
    }

    public void setHighlight(int r, int c) {
        this.highlightR = r;
        this.highlightC = c;
        repaint();
    }

    public void clearMatrix() {
        this.distances = null;
        this.N = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (distances == null || N == 0) {
            drawEmpty(g);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int labelW = 36;
        int headerH = 24;
        int cellW = Math.max(48, (getWidth() - labelW) / (N + 1));
        int cellH = Math.max(24, (getHeight() - headerH) / (N + 1));
        int tableW = labelW + N * cellW;
        int tableH = headerH + N * cellH;
        int startX = (getWidth() - tableW) / 2;
        int startY = (getHeight() - tableH) / 2;
        if (startX < 0) startX = 0;
        if (startY < 0) startY = 0;

        g2.setFont(new Font("Consolas", Font.BOLD, 10));
        FontMetrics fm = g2.getFontMetrics();

        g2.setColor(HEADER_FG);
        g2.drawString("Floyd-Warshall", startX, startY - 6);

        for (int c = 0; c < N; c++) {
            int x = startX + labelW + c * cellW + cellW / 2;
            int y = startY + headerH - 6;
            String text = String.valueOf(c);
            g2.setColor(HEADER_FG);
            g2.drawString(text, x - fm.stringWidth(text) / 2, y);
        }

        for (int r = 0; r < N; r++) {
            int y = startY + headerH + r * cellH + cellH / 2 + 4;
            String text = String.valueOf(r);
            g2.setColor(HEADER_FG);
            g2.drawString(text, startX + labelW / 2 - fm.stringWidth(text) / 2, y);
        }

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                int x = startX + labelW + c * cellW;
                int y = startY + headerH + r * cellH;

                if (r == highlightR && c == highlightC) {
                    g2.setColor(HIGHLIGHT_BG);
                    g2.fillRect(x, y, cellW, cellH);
                }

                g2.setColor(GRID_LINE);
                g2.drawRect(x, y, cellW, cellH);

                String text;
                Color color;
                if (unbounded[r][c]) {
                    text = "inf";
                    color = UNBounded_FG;
                } else if (distances[r][c] == Long.MIN_VALUE) {
                    text = "-";
                    color = NO_ROUTE_FG;
                } else {
                    text = String.valueOf(distances[r][c]);
                    color = CELL_FG;
                }

                g2.setColor(color);
                g2.setFont(new Font("Consolas", Font.PLAIN, 11));
                FontMetrics cfm = g2.getFontMetrics();
                int tw = cfm.stringWidth(text);
                g2.drawString(text, x + (cellW - tw) / 2, y + (cellH + cfm.getAscent() - cfm.getDescent()) / 2);
            }
        }

        g2.dispose();
    }

    private void drawEmpty(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(NO_ROUTE_FG);
        g2.setFont(new Font("Consolas", Font.ITALIC, 12));
        String msg = "Ejecuta para ver la matriz";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
    }
}
