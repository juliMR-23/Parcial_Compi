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
    }

    @Override
    protected String getSampleInput() {
        return SAMPLE;
    }

    @Override
    protected void onRun() {
        execute(() -> {
            List<DijkstraCase> cases = DijkstraParser.parse(inputArea.getText());
            StringBuilder sb = new StringBuilder();
            int caseNum = 1;

            for (DijkstraCase dc : cases) {
                DijkstraSolver solver = new DijkstraSolver(dc);
                long result = solver.solve();

                sb.append("Case #").append(caseNum).append(": ");
                if (result == -1) {
                    sb.append("Nina is very sad");
                } else {
                    sb.append(result);
                }
                sb.append("\n");

                if (caseNum == 1) {
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
                    canvas.setHighlightedEdges(solver.getPathEdges());
                }

                caseNum++;
            }

            return sb.toString().trim();
        });
    }
}
