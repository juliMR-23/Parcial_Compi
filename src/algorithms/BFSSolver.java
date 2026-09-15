package algorithms;

import parsers.MinefieldCase;

import java.util.*;

public class BFSSolver {

    private final MinefieldCase mc;
    private List<int[]> lastPath = new ArrayList<>();

    public BFSSolver(MinefieldCase mc) {
        this.mc = mc;
    }

    public List<int[]> getPath() {
        return lastPath;
    }

    public int bfs() {
        lastPath = new ArrayList<>();
        if (mc.bomb[mc.startRow][mc.startCol] || mc.bomb[mc.finalRow][mc.finalCol]) return -1;

        int[][] dist = new int[mc.R][mc.C];
        int[][][] parent = new int[mc.R][mc.C][2];
        for (int[] row : dist) Arrays.fill(row, -1);
        for (int r = 0; r < mc.R; r++)
            for (int c = 0; c < mc.C; c++)
                parent[r][c] = new int[]{-1, -1};

        Queue<int[]> queue = new LinkedList<>();
        dist[mc.startRow][mc.startCol] = 0;
        queue.offer(new int[]{mc.startRow, mc.startCol});

        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int i = 0; i < 4; i++) {
                int y = cur[0] + dRow[i];
                int x = cur[1] + dCol[i];
                if (y < 0 || y >= mc.R || x < 0 || x >= mc.C) continue;
                if (mc.bomb[y][x]) continue;
                if (dist[y][x] != -1) continue;
                dist[y][x] = dist[cur[0]][cur[1]] + 1;
                parent[y][x] = new int[]{cur[0], cur[1]};
                queue.add(new int[]{y, x});
            }
        }

        int result = dist[mc.finalRow][mc.finalCol];
        if (result != -1) {
            lastPath = reconstructPath(parent, mc.finalRow, mc.finalCol);
        }
        return result;
    }

    private List<int[]> reconstructPath(int[][][] parent, int er, int ec) {
        List<int[]> path = new ArrayList<>();
        int r = er, c = ec;
        while (r != -1 && c != -1) {
            path.add(new int[]{r, c});
            int[] p = parent[r][c];
            r = p[0];
            c = p[1];
        }
        Collections.reverse(path);
        return path;
    }
}
