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
            List<Mission3Case> cases = Mission3Parser.parse(inputArea.getText());
            StringBuilder sb = new StringBuilder();
            int caseNum = 1;

            for (Mission3Case mc : cases) {
                FloydWarshallSolver floyd = new FloydWarshallSolver(mc.N);
                BellmanFordSolver bellman = new BellmanFordSolver(mc.N);

                for (Edge e : mc.edges) {
                    floyd.addEdge(e.getSource(), e.getTarget(), e.getWeight());
                    bellman.addEdge(e.getSource(), e.getTarget(), e.getWeight());
                }

                long[][] floydResult = floyd.solve();
                long[] bellmanResult = bellman.solve(mc.S);

                long maxChurun = floydResult[mc.S][mc.D];

                sb.append("Case #").append(caseNum).append(": ");
                if (bellmanResult[mc.D] == Long.MAX_VALUE) {
                    sb.append("Limon blocked the way");
                } else if (maxChurun == Long.MAX_VALUE) {
                    sb.append("Infinite churun!");
                } else {
                    sb.append(maxChurun);
                }
                sb.append("\n");
                caseNum++;
            }

            if (caseNum == 2) {
                Mission3Case mc = cases.get(0);
                Graph graph = new Graph(true);
                for (int i = 0; i < mc.N; i++) {
                    graph.addNode(new Node(i));
                }
                for (Edge e : mc.edges) {
                    graph.addEdge(e);
                }
                canvas.setData(graph.getNodes(), graph.getEdges(), true);
            }

            return sb.toString().trim();
        });
    }
}
