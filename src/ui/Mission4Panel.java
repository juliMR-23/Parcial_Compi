package ui;

import algorithms.KruskalSolver;
import models.Edge;
import models.Graph;
import models.Node;
import parsers.Mission4Case;
import parsers.Mission4Parser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Mission4Panel extends MissionPanel {

    private static final Color ACCENT = new Color(5, 255, 161);
    private final GraphCanvas canvas;

    private List<Mission4Case> cases;
    private List<Long> results = new ArrayList<>();
    private List<List<Edge>> mstEdgesList = new ArrayList<>();

    private static final String SAMPLE = """
        1
        4
        5
        1 2 10
        2 3 20
        3 4 30
        4 1 40
        1 3 15""";

    public Mission4Panel(App app) {
        super(app, ACCENT);
        canvas = new GraphCanvas();
        add(new JScrollPane(canvas), BorderLayout.EAST);
        canvas.setPreferredSize(new Dimension(620, 0));

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
            cases = Mission4Parser.parse(inputArea.getText());
            results.clear();
            mstEdgesList.clear();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < cases.size(); i++) {
                Mission4Case mc = cases.get(i);
                KruskalSolver solver = new KruskalSolver(mc.N);
                for (Edge e : mc.edges) {
                    solver.addEdge(e.getSource(), e.getTarget(), e.getWeight());
                }

                long result = solver.kruskal();
                results.add(result);
                mstEdgesList.add(solver.getMstEdges());

                sb.append("Case #").append(i + 1).append(": ");
                if (result == -1) {
                    sb.append("Limón cut too many cables");
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
        Mission4Case mc = cases.get(idx);
        Graph graph = new Graph(false);
        for (int i = 1; i <= mc.N; i++) {
            graph.addNode(new Node(i));
        }
        for (Edge e : mc.edges) {
            graph.addEdge(e);
        }
        canvas.setData(graph.getNodes(), graph.getEdges(), false);
        canvas.setHighlightedEdges(new ArrayList<>());
        canvas.clearStatusMessage();

        if (results.get(idx) == -1) {
            canvas.setStatusMessage("Limón cut too many cables");
        } else {
            canvas.setHighlightedEdges(mstEdgesList.get(idx));
        }
    }
}
