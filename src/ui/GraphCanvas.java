package ui;

import models.Edge;
import models.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphCanvas extends JPanel {

    private static final Color BG = new Color(10, 10, 26);
    private static final Color NODE_FILL = new Color(45, 55, 110);      // azul más saturado (antes 40,40,70)
    private static final Color NODE_STROKE = new Color(140, 160, 255);  // borde violeta claro (antes 100,100,140)
    private static final Color NODE_TEXT = Color.WHITE;
    private static final Color EDGE_DEFAULT = new Color(110, 120, 200); // arista base más visible (antes 70,70,100)
    private static final Color EDGE_HIGHLIGHT = new Color(0, 255, 245);   // cyan neon (sin cambios)
    private static final Color EDGE_MST = new Color(5, 255, 161);        // green neon (sin cambios)
    private static final Color EDGE_CYCLE = new Color(255, 149, 0);      // amber neon (sin cambios)
    private static final Color EDGE_DISCARDED = new Color(90, 95, 130);  // gris-azul, pero más claro (antes 60,60,90)
    private static final Color WEIGHT_TEXT = new Color(220, 225, 255);   // texto de peso más blanco (antes 200,200,220)
    private static final Color LABEL_TEXT = new Color(0, 255, 245);
    private static final Color ERROR_TEXT = new Color(255, 42, 109);
    private static final Color TEXT_DIM = new Color(160, 165, 210);      // más legible (antes 120,120,160)
    private static final Color STATUS_TEXT = new Color(255, 149, 0);

    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();
    private List<Edge> highlightedEdges = new ArrayList<>();
    private List<Edge> cycleEdges = new ArrayList<>();
    private boolean directed = false;
    private String nodeLabel = null;
    private String errorOverride = null;
    private String statusMessage = null;

    public GraphCanvas() {
        setBackground(BG);
    }

    public void setData(List<Node> nodes, List<Edge> edges, boolean directed) {
        this.nodes = nodes != null ? nodes : new ArrayList<>();
        this.edges = edges != null ? edges : new ArrayList<>();
        this.directed = directed;
        autoLayout();
        repaint();
    }

    public void setHighlightedEdges(List<Edge> edges) {
        this.highlightedEdges = edges != null ? edges : new ArrayList<>();
        repaint();
    }

    public void setCycleEdges(List<Edge> edges) {
        this.cycleEdges = edges != null ? edges : new ArrayList<>();
        repaint();
    }

    public void setNodeLabel(String label) {
        this.nodeLabel = label;
    }

    public void showError(String message) {
        this.errorOverride = message;
        repaint();
    }

    public void clearError() {
        this.errorOverride = null;
        repaint();
    }

    public void setStatusMessage(String message) {
        this.statusMessage = message;
        repaint();
    }

    public void clearStatusMessage() {
        this.statusMessage = null;
        repaint();
    }

    public void clearAll() {
        nodes.clear();
        edges.clear();
        highlightedEdges.clear();
        cycleEdges.clear();
        errorOverride = null;
        statusMessage = null;
        nodeLabel = null;
        repaint();
    }

    private void autoLayout() {
        if (nodes.isEmpty()) return;
        double cx = getWidth() / 2.0;
        double cy = getHeight() / 2.0;
        double radius = Math.min(getWidth(), getHeight()) / 2.8;

        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            if (n.getX() == 0 && n.getY() == 0) {
                double angle = 2 * Math.PI * i / nodes.size() - Math.PI / 2;
                n.setX(cx + radius * Math.cos(angle));
                n.setY(cy + radius * Math.sin(angle));
            }
        }
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

        if (nodes.isEmpty()) {
            drawEmpty(g2);
            g2.dispose();
            return;
        }

        drawEdges(g2);
        drawNodes(g2);
        if (statusMessage != null) {
            drawStatus(g2);
        }
        g2.dispose();
    }

    private void drawEdges(Graphics2D g2) {
        for (Edge e : edges) {
            Node from = findNode(e.getSource());
            Node to = findNode(e.getTarget());
            if (from == null || to == null) continue;
            if (from.getId() == to.getId()) continue;

            Color color;
            Stroke stroke;

            if (cycleEdges.contains(e)) {
                color = EDGE_CYCLE;
                stroke = new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            } else if (highlightedEdges.contains(e)) {
                color = EDGE_HIGHLIGHT;
                stroke = new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            } else if (isDiscarded(e)) {
                color = EDGE_DISCARDED;
                stroke = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{6f, 4f}, 0f);
            } else {
                color = EDGE_DEFAULT;
                stroke = new BasicStroke(1.5f);
            }

            g2.setColor(color);
            g2.setStroke(stroke);

            if (directed) {
                drawArrow(g2, from.getX(), from.getY(), to.getX(), to.getY(), color);
            } else {
                g2.drawLine((int) from.getX(), (int) from.getY(), (int) to.getX(), (int) to.getY());
            }

            drawWeight(g2, from, to, e.getWeight());
        }
    }

    private void drawArrow(Graphics2D g2, double x1, double y1, double x2, double y2, Color color) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) return;

        double nodeRadius = 20;
        double ux = dx / dist;
        double uy = dy / dist;

        double startX = x1 + ux * nodeRadius;
        double startY = y1 + uy * nodeRadius;
        double endX = x2 - ux * nodeRadius;
        double endY = y2 - uy * nodeRadius;

        g2.drawLine((int) startX, (int) startY, (int) endX, (int) endY);

        double arrowLen = 12;
        double arrowAngle = Math.toRadians(25);
        double ax = endX - arrowLen * Math.cos(Math.atan2(dy, dx) - arrowAngle);
        double ay = endY - arrowLen * Math.sin(Math.atan2(dy, dx) - arrowAngle);
        double bx = endX - arrowLen * Math.cos(Math.atan2(dy, dx) + arrowAngle);
        double by = endY - arrowLen * Math.sin(Math.atan2(dy, dx) + arrowAngle);

        g2.fillPolygon(
            new int[]{(int) endX, (int) ax, (int) bx},
            new int[]{(int) endY, (int) ay, (int) by},
            3
        );
    }

    private void drawWeight(Graphics2D g2, Node from, Node to, int weight) {
        double mx = (from.getX() + to.getX()) / 2;
        double my = (from.getY() + to.getY()) / 2;
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) return;

        double offsetX = -dy / dist * 14;
        double offsetY = dx / dist * 14;

        g2.setFont(new Font("Consolas", Font.BOLD, 11));
        String text = String.valueOf(weight);
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(text);

        g2.setColor(BG);
        g2.fillRect((int) (mx + offsetX - textW / 2 - 2), (int) (my + offsetY - 7), textW + 4, 14);
        g2.setColor(WEIGHT_TEXT);
        g2.drawString(text, (int) (mx + offsetX - textW / 2), (int) (my + offsetY + 4));
    }

    private void drawNodes(Graphics2D g2) {
        int radius = 20;

        for (Node n : nodes) {
            boolean isSpecial = nodeLabel != null && String.valueOf(n.getId()).equals(nodeLabel);

            g2.setColor(isSpecial ? LABEL_TEXT : NODE_FILL);
            g2.fillOval((int) n.getX() - radius, (int) n.getY() - radius, radius * 2, radius * 2);

            g2.setColor(NODE_STROKE);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval((int) n.getX() - radius, (int) n.getY() - radius, radius * 2, radius * 2);

            g2.setColor(NODE_TEXT);
            g2.setFont(new Font("Consolas", Font.BOLD, 13));
            String label = String.valueOf(n.getId());
            FontMetrics fm = g2.getFontMetrics();
            int textW = fm.stringWidth(label);
            g2.drawString(label, (int) n.getX() - textW / 2, (int) n.getY() + 5);
        }
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

    private Node findNode(int id) {
        for (Node n : nodes) {
            if (n.getId() == id) return n;
        }
        return null;
    }

    private boolean isDiscarded(Edge e) {
        for (Edge h : highlightedEdges) {
            if (h.getSource() == e.getSource() && h.getTarget() == e.getTarget()) return false;
            if (h.getSource() == e.getTarget() && h.getTarget() == e.getSource()) return false;
        }
        for (Edge c : cycleEdges) {
            if (c.getSource() == e.getSource() && c.getTarget() == e.getTarget()) return false;
        }
        return true;
    }

    private void drawStatus(Graphics2D g2) {
        g2.setFont(new Font("Consolas", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(statusMessage);
        int x = (getWidth() - textW) / 2;
        int y = getHeight() - 20;

        g2.setColor(BG);
        g2.fillRoundRect(x - 8, y - fm.getAscent() - 4, textW + 16, fm.getHeight() + 8, 8, 8);
        g2.setColor(STATUS_TEXT);
        g2.drawString(statusMessage, x, y);
    }
}
