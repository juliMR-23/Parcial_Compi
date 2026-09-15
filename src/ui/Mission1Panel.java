package ui;

import algorithms.BFSSolver;
import algorithms.DFSSolver;
import parsers.MineFieldParser;
import parsers.MinefieldCase;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Mission1Panel extends MissionPanel {

    private static final Color ACCENT = new Color(255, 149, 0);
    private final GridCanvas canvas;

    private static final String SAMPLE = """
        10 10
        9
        0 1 2
        1 1 2
        2 2 2 9
        3 2 1 7
        5 3 3 6 9
        6 4 0 1 2 7
        7 3 0 3 8
        8 2 7 9
        9 3 2 3 4
        0 0
        9 9
        0 0""";

    public Mission1Panel(App app) {
        super(app, ACCENT);
        canvas = new GridCanvas();
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
            List<MinefieldCase> cases = MineFieldParser.parse(inputArea.getText());
            StringBuilder sb = new StringBuilder();
            int caseNum = 1;

            for (MinefieldCase mc : cases) {
                BFSSolver bfsSolver = new BFSSolver(mc);
                DFSSolver dfsSolver = new DFSSolver(mc);

                int bfsResult = bfsSolver.bfs();
                int dfsResult = dfsSolver.dfs();

                if (bfsResult == -1 && dfsResult == -1) {
                    sb.append("Case #").append(caseNum).append(": Nina is unreachable");
                } else {
                    sb.append("Case #").append(caseNum).append(": BFS ");
                    sb.append(bfsResult == -1 ? "unreachable" : bfsResult);
                    sb.append(" DFS ");
                    sb.append(dfsResult == -1 ? "unreachable" : dfsResult);
                }
                sb.append("\n");

                if (caseNum == 1) {
                    MinefieldCase mc0 = cases.get(0);
                    canvas.setGrid(mc0.R, mc0.C, mc0.bomb, mc0.startRow, mc0.startCol, mc0.finalRow, mc0.finalCol);
                    canvas.setBfsPath(bfsSolver.getPath());
                    canvas.setDfsPath(dfsSolver.getTraversalOrder());
                    canvas.setShowDfsNumbers(true);
                }

                caseNum++;
            }

            return sb.toString().trim();
        });
    }
}
