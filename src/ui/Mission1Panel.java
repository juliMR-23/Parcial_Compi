package ui;

import algorithms.BFSSolver;
import algorithms.DFSSolver;
import parsers.MineFieldParser;
import parsers.MinefieldCase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Mission1Panel extends MissionPanel {

    private static final Color ACCENT = new Color(255, 149, 0);
    private final GridCanvas canvas;

    private List<MinefieldCase> cases;
    private List<Integer> bfsResults = new ArrayList<>();
    private List<Integer> dfsResults = new ArrayList<>();
    private List<List<int[]>> bfsPaths = new ArrayList<>();
    private List<List<int[]>> dfsOrders = new ArrayList<>();

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
            cases = MineFieldParser.parse(inputArea.getText());
            bfsResults.clear();
            dfsResults.clear();
            bfsPaths.clear();
            dfsOrders.clear();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < cases.size(); i++) {
                MinefieldCase mc = cases.get(i);
                BFSSolver bfsSolver = new BFSSolver(mc);
                DFSSolver dfsSolver = new DFSSolver(mc);

                int bfsResult = bfsSolver.bfs();
                int dfsResult = dfsSolver.dfs();

                bfsResults.add(bfsResult);
                dfsResults.add(dfsResult);
                bfsPaths.add(bfsSolver.getPath());
                dfsOrders.add(dfsSolver.getTraversalOrder());

                sb.append("Case #").append(i + 1).append(": ");
                if (bfsResult == -1 && dfsResult == -1) {
                    sb.append("Nina is unreachable");
                } else {
                    sb.append("BFS ");
                    sb.append(bfsResult == -1 ? "unreachable" : bfsResult);
                    sb.append(" DFS ");
                    sb.append(dfsResult == -1 ? "unreachable" : dfsResult);
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
        MinefieldCase mc = cases.get(idx);
        canvas.setGrid(mc.R, mc.C, mc.bomb, mc.startRow, mc.startCol, mc.finalRow, mc.finalCol);
        canvas.setBfsPath(bfsPaths.get(idx));
        canvas.setDfsPath(dfsOrders.get(idx));
        canvas.setShowDfsNumbers(true);
    }
}
