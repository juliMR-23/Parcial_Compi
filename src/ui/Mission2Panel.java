package ui;

import algorithms.DijkstraSolver;
import models.Edge;
import models.Graph;
import models.Node;
import parsers.DijkstraCase;
import parsers.DijkstraParser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Mission2Panel extends MissionPanel {

    private static final Color ACCENT = new Color(191, 0, 255);
    private final GraphCanvas canvas;

    private List<DijkstraCase> cases;
    private List<Long> results = new ArrayList<>();
    private List<List<Edge>> pathEdges = new ArrayList<>();

    private static final String SAMPLE = """
        3
        2 1 0 1
        0 1 100
        3 3 2 0
        0 1 100
        0 2 200
        1 2 50
        2 0 0 1""";

    public Mission2Panel(App app) {
        super(app, ACCENT);
        canvas = new GraphCanvas();
        add(new JScrollPane(canvas), BorderLayout.EAST);
        canvas.setPreferredSize(new Dimension(500, 0));

        caseSelector.addActionListener(e -> {
            int idx = caseSelector.getSelectedIndex();
            if (idx >= 0 && cases != null && idx < cases.size()) {
                showCase(idx);
            }
        });
    }

    @Override
    protected String getSampleInput() {
        return SAMPLE;
    }

    @Override
    protected void onRun() {
        execute(() -> {
            cases = DijkstraParser.parse(inputArea.getText());
            results.clear();
            pathEdges.clear();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < cases.size(); i++) {
                DijkstraCase dc = cases.get(i);
                DijkstraSolver solver = new DijkstraSolver(dc);
                long result = solver.solve();

                results.add(result);
                pathEdges.add(solver.getPathEdges());

                sb.append("Case #").append(i + 1).append(": ");
                if (result == -1) {
                    sb.append("Nina is very sad");
                } else {
                    sb.append(result);
                }
                sb.append("\n");
            }

            caseSelector.removeActionListener(caseSelector.getActionListeners().length > 0 ? caseSelector.getActionListeners()[0] : null);
            caseSelector.removeAllItems();
            for (int i = 0; i < cases.size(); i++) {
                caseSelector.addItem("Case #" + (i + 1));
            }
            caseSelector.setSelectedIndex(0);
            caseSelector.setVisible(true);
            caseSelector.addActionListener(e -> {
                int idx = caseSelector.getSelectedIndex();
                if (idx >= 0 && cases != null && idx < cases.size()) {
                    showCase(idx);
                }
            });

            showCase(0);

            return sb.toString().trim();
        });
    }

    private void showCase(int idx) {
        DijkstraCase dc = cases.get(idx);
        Graph graph = new Graph(false);
        for (int i = 0; i < dc.N; i++) {
            graph.addNode(new Node(i));
        }
        for (int i = 0; i < dc.N; i++) {
            for (Edge e : dc.adj[i]) {
                if (e.getSource() < e.getTarget()) {
                    graph.addEdge(e);
                }
            }
        }
        canvas.setData(graph.getNodes(), graph.getEdges(), false);
        canvas.clearStatusMessage();

        if (results.get(idx) == -1) {
            canvas.setStatusMessage("Sin camino disponible");
        } else {
            canvas.setHighlightedEdges(pathEdges.get(idx));
        }
    }
}
