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
        canvas.setPreferredSize(new Dimension(500, 0));
    }

    @Override
    protected String getSampleInput() {
        return SAMPLE;
    }

    @Override
    protected void onRun() {
        execute(() -> {
            List<Mission4Case> cases = Mission4Parser.parse(inputArea.getText());
            StringBuilder sb = new StringBuilder();
            int caseNum = 1;

            for (Mission4Case mc : cases) {
                KruskalSolver solver = new KruskalSolver(mc.N);
                for (Edge e : mc.edges) {
                    solver.addEdge(e.getSource(), e.getTarget(), e.getWeight());
                }

                long result = solver.kruskal();

                sb.append("Case #").append(caseNum).append(": ");
                if (result == -1) {
                    sb.append("Limon cut too many cables");
                } else {
                    sb.append(result);
                }
                sb.append("\n");

                if (caseNum == 1) {
                    Graph graph = new Graph(false);
                    for (int i = 1; i <= mc.N; i++) {
                        graph.addNode(new Node(i));
                    }
                    for (Edge e : mc.edges) {
                        graph.addEdge(e);
                    }
                    canvas.setData(graph.getNodes(), graph.getEdges(), false);

                    if (result != -1) {
                        canvas.setHighlightedEdges(solver.getMstEdges());
                    }
                }

                caseNum++;
            }

            return sb.toString().trim();
        });
    }
}
