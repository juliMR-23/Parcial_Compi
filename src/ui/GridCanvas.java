package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GridCanvas extends JPanel {

    private static final Color BG = new Color(10, 10, 26);
    private static final Color CELL_EMPTY = new Color(22, 22, 50);
    private static final Color CELL_WALL = new Color(231, 76, 60);
    private static final Color CELL_START = new Color(0, 255, 245);
    private static final Color CELL_END = new Color(255, 42, 109);
    private static final Color CELL_BFS = new Color(5, 255, 161);
    private static final Color CELL_DFS = new Color(100, 100, 255);
    private static final Color CELL_TEXT = Color.WHITE;
    private static final Color GRID_LINE = new Color(40, 40, 70);
    private static final Color ERROR_TEXT = new Color(255, 42, 109);
    private static final Color TEXT_DIM = new Color(120, 120, 160);

    private int rows = 0;
    private int cols = 0;
    private boolean[][] bombs = null;
    private int startRow = -1, startCol = -1;
    private int endRow = -1, endCol = -1;
    private List<int[]> bfsPath = new ArrayList<>();
    private List<int[]> dfsPath = new ArrayList<>();
    private boolean showDfsNumbers = false;
    private String errorOverride = null;

    public GridCanvas() {
        setBackground(BG);
    }

    public void setGrid(int rows, int cols, boolean[][] bombs, int sr, int sc, int er, int ec) {
        this.rows = rows;
        this.cols = cols;
        this.bombs = bombs;
        this.startRow = sr;
        this.startCol = sc;
        this.endRow = er;
        this.endCol = ec;
        this.errorOverride = null;
        repaint();
    }

    public void setBfsPath(List<int[]> path) {
        this.bfsPath = path != null ? path : new ArrayList<>();
        repaint();
    }

    public void setDfsPath(List<int[]> path) {
        this.dfsPath = path != null ? path : new ArrayList<>();
        repaint();
    }

    public void setShowDfsNumbers(boolean show) {
        this.showDfsNumbers = show;
        repaint();
    }

    public void showError(String message) {
        this.errorOverride = message;
        repaint();
    }

    public void clearAll() {
        rows = 0;
        cols = 0;
        bombs = null;
        bfsPath.clear();
        dfsPath.clear();
        errorOverride = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getWidth() == 0 || getHeight() == 0) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (errorOverride != null) {
            drawError(g2);
            g2.dispose();
            return;
        }

        if (rows == 0 || cols == 0 || bombs == null) {
            drawEmpty(g2);
            g2.dispose();
            return;
        }

        drawGrid(g2);
        g2.dispose();
    }

    private void drawGrid(Graphics2D g2) {
        double margin = 30;
        double cellW = (getWidth() - margin * 2) / cols;
        double cellH = (getHeight() - margin * 2) / rows;
        double cellSize = Math.min(cellW, cellH);

        double offsetX = (getWidth() - cellSize * cols) / 2;
        double offsetY = (getHeight() - cellSize * rows) / 2;

        g2.setFont(new Font("Consolas", Font.PLAIN, Math.max(9, (int) (cellSize * 0.35))));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x = offsetX + c * cellSize;
                double y = offsetY + r * cellSize;

                Color fillColor = CELL_EMPTY;
                if (bombs[r][c]) {
                    fillColor = CELL_WALL;
                } else if (r == startRow && c == startCol) {
                    fillColor = CELL_START;
                } else if (r == endRow && c == endCol) {
                    fillColor = CELL_END;
                } else if (isInBfsPath(r, c)) {
                    fillColor = CELL_BFS;
                } else if (isInDfsPath(r, c)) {
                    fillColor = CELL_DFS;
                }

                g2.setColor(fillColor);
                g2.fillRect((int) x + 1, (int) y + 1, (int) cellSize - 2, (int) cellSize - 2);

                g2.setColor(GRID_LINE);
                g2.setStroke(new BasicStroke(0.5f));
                g2.drawRect((int) x, (int) y, (int) cellSize, (int) cellSize);

                drawCellLabel(g2, r, c, x, y, cellSize, fillColor);
            }
        }
    }

    private void drawCellLabel(Graphics2D g2, int r, int c, double x, double y, double cellSize, Color fillColor) {
        if (cellSize < 25) return;

        String label = null;
        if (r == startRow && c == startCol) {
            label = "S";
        } else if (r == endRow && c == endCol) {
            label = "N";
        } else if (bombs[r][c]) {
            label = "X";
        } else if (showDfsNumbers && isInDfsPath(r, c)) {
            label = String.valueOf(getDfsOrder(r, c));
        }

        if (label == null) return;

        g2.setFont(new Font("Consolas", Font.BOLD, Math.max(10, (int) (cellSize * 0.4))));
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(label);

        g2.setColor(CELL_TEXT);
        g2.drawString(label, (int) (x + cellSize / 2 - textW / 2), (int) (y + cellSize / 2 + fm.getAscent() / 3));
    }

    private boolean isInBfsPath(int r, int c) {
        for (int[] pos : bfsPath) {
            if (pos[0] == r && pos[1] == c) return true;
        }
        return false;
    }

    private boolean isInDfsPath(int r, int c) {
        for (int[] pos : dfsPath) {
            if (pos[0] == r && pos[1] == c) return true;
        }
        return false;
    }

    private int getDfsOrder(int r, int c) {
        for (int i = 0; i < dfsPath.size(); i++) {
            if (dfsPath.get(i)[0] == r && dfsPath.get(i)[1] == c) return i;
        }
        return -1;
    }

    private void drawError(Graphics2D g2) {
        g2.setColor(ERROR_TEXT);
        g2.setFont(new Font("Consolas", Font.BOLD, 16));
        String line1 = "[OMITIDO POR TAMAÑO]";
        FontMetrics fm = g2.getFontMetrics();
        int w1 = fm.stringWidth(line1);
        g2.drawString(line1, (getWidth() - w1) / 2, getHeight() / 2 - 15);

        g2.setColor(TEXT_DIM);
        g2.setFont(new Font("Consolas", Font.PLAIN, 12));
        fm = g2.getFontMetrics();
        int w2 = fm.stringWidth(errorOverride);
        g2.drawString(errorOverride, (getWidth() - w2) / 2, getHeight() / 2 + 15);
    }

    private void drawEmpty(Graphics2D g2) {
        g2.setColor(TEXT_DIM);
        g2.setFont(new Font("Consolas", Font.ITALIC, 13));
        String msg = "Sin datos para graficar";
        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(msg);
        g2.drawString(msg, (getWidth() - w) / 2, getHeight() / 2);
    }
}
