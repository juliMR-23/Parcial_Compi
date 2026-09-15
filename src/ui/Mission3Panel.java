package ui;

import algorithms.BellmanFordSolver;
import algorithms.FloydWarshallSolver;
import models.Edge;
import models.Graph;
import models.Node;
import parsers.Mission3Case;
import parsers.Mission3Parser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Mission3Panel extends MissionPanel {

    private static final Color ACCENT = new Color(255, 42, 109);
    private final GraphCanvas canvas;
    private final FloydMatrixCanvas matrixCanvas;

    private List<Mission3Case> cases;
    private List<Long> maxChuruns = new ArrayList<>();
    private List<Boolean> hasInfinite = new ArrayList<>();
    private List<Boolean> isBlocked = new ArrayList<>();
    private List<FloydWarshallSolver> floydSolvers = new ArrayList<>();
    private List<BellmanFordSolver> bellmanSolvers = new ArrayList<>();

    private static final String SAMPLE = """
        3
        5 7 0 4
        0 1 50
        0 2 10
        1 2 -30
        1 3 40
        2 1 -5
        2 3 60
        3 4 20
        4 4 0 3
        0 1 20
        1 2 30
        2 1 -10
        2 3 15
        3 3 0 2
        0 1 -40
        1 2 -25
        0 2 -80""";

    public Mission3Panel(App app) {
        super(app, ACCENT);
        canvas = new GraphCanvas();
        matrixCanvas = new FloydMatrixCanvas();

        JSplitPane rightPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(canvas), new JScrollPane(matrixCanvas));
        rightPanel.setDividerLocation(300);
        rightPanel.setResizeWeight(0.6);
        rightPanel.setOpaque(false);
        rightPanel.setBorder(null);
        add(rightPanel, BorderLayout.EAST);
        rightPanel.setPreferredSize(new Dimension(620, 0));

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
            cases = Mission3Parser.parse(inputArea.getText());
            maxChuruns.clear();
            hasInfinite.clear();
            isBlocked.clear();
            floydSolvers.clear();
            bellmanSolvers.clear();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < cases.size(); i++) {
                Mission3Case mc = cases.get(i);
                FloydWarshallSolver floyd = new FloydWarshallSolver(mc.N);
                BellmanFordSolver bellman = new BellmanFordSolver(mc.N);

                for (Edge e : mc.edges) {
                    floyd.addEdge(e.getSource(), e.getTarget(), e.getWeight());
                    bellman.addEdge(e.getSource(), e.getTarget(), e.getWeight());
                }

                long[][] floydResult = floyd.solve();
                long[] bellmanResult = bellman.solve(mc.S);
                boolean floydUnbounded = floyd.isUnbounded(mc.S, mc.D);
                boolean bellmanAffected = bellman.isAffectedByPositiveCycle(mc.D);

                floydSolvers.add(floyd);
                bellmanSolvers.add(bellman);
                maxChuruns.add(floydResult[mc.S][mc.D]);
                hasInfinite.add(floydUnbounded || bellmanAffected);
                isBlocked.add(!hasInfinite.get(i) && bellmanResult[mc.D] == Long.MIN_VALUE);

                boolean mismatch = floydUnbounded != bellmanAffected;

                sb.append("Case #").append(i + 1).append(": ");
                if (isBlocked.get(i)) {
                    sb.append("Limon blocked the way");
                } else if (hasInfinite.get(i)) {
                    sb.append("Infinite churun!");
                } else {
                    sb.append(maxChuruns.get(i));
                }
                sb.append("\n");

                if (mismatch) {
                    sb.append("  ** MISMATCH: Floyd=").append(floydUnbounded ? "inf" : floydResult[mc.S][mc.D])
                      .append(" Bellman=").append(bellmanAffected ? "inf" : bellmanResult[mc.D]).append("\n");
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
        Mission3Case mc = cases.get(idx);
        Graph graph = new Graph(true);
        for (int i = 0; i < mc.N; i++) {
            graph.addNode(new Node(i));
        }
        for (Edge e : mc.edges) {
            graph.addEdge(e);
        }
        canvas.setData(graph.getNodes(), graph.getEdges(), true);
        canvas.setCycleEdges(new ArrayList<>());
        canvas.setHighlightedEdges(new ArrayList<>());
        canvas.clearStatusMessage();

        FloydWarshallSolver floyd = floydSolvers.get(idx);
        matrixCanvas.setMatrix(floyd, floyd.getDistances(), mc.N);

        if (isBlocked.get(idx)) {
            canvas.setStatusMessage("Limon blocked the way");
        } else if (hasInfinite.get(idx)) {
            canvas.setCycleEdges(floyd.getCycleEdges());
            canvas.setStatusMessage("Infinite churun!");
            matrixCanvas.setHighlight(mc.S, mc.D);
        } else if (maxChuruns.get(idx) != Long.MIN_VALUE) {
            canvas.setHighlightedEdges(floyd.getPathEdges(mc.S, mc.D));
            matrixCanvas.setHighlight(mc.S, mc.D);
        }
    }
}
