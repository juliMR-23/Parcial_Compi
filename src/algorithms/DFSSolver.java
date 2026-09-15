package algorithms;

import parsers.MinefieldCase;

import java.util.*;

/**
 * DFS (Depth-First Search) iterativo para grilla de minas.
 *
 * Complejidad temporal:  O(R × C) — cada celda se visita a lo sumo una vez.
 * Complejidad espacial: O(R × C) — matriz visited + pila de exploración.
 *
 * Elegido porque: el enunciado requiere DFS (exploración en profundidad) y
 * que funcione con grillas de hasta 10^6 celdas. La versión recursiva causaría
 * StackOverflowError; la iterativa usa ArrayDeque como pila explícita en heap.
 */

public class DFSSolver {

    private final MinefieldCase mc;
    private List<int[]> lastTraversalOrder = new ArrayList<>();

    public DFSSolver(MinefieldCase mc) {
        this.mc = mc;
    }

    public List<int[]> getTraversalOrder() {
        return lastTraversalOrder;
    }

    public int dfs() {
        lastTraversalOrder = new ArrayList<>();
        if (mc.bomb[mc.startRow][mc.startCol] || mc.bomb[mc.finalRow][mc.finalCol]) return -1;

        boolean[][] visited = new boolean[mc.R][mc.C];
        int[][] parentRow = new int[mc.R][mc.C];
        int[][] parentCol = new int[mc.R][mc.C];
        for (int[] row : parentRow) Arrays.fill(row, -1);
        for (int[] row : parentCol) Arrays.fill(row, -1);

        Deque<int[]> deque = new ArrayDeque<>();
        deque.push(new int[]{mc.startRow, mc.startCol, 0});

        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        while (!deque.isEmpty()) {
            int[] cur = deque.pop();
            if (visited[cur[0]][cur[1]]) continue;

            visited[cur[0]][cur[1]] = true;

            if (cur[0] == mc.finalRow && cur[1] == mc.finalCol) {
                lastTraversalOrder = reconstructPath(parentRow, parentCol, cur[0], cur[1]);
                return cur[2];
            }

            for (int i = 3; i >= 0; i--) {
                int y = cur[0] + dRow[i];
                int x = cur[1] + dCol[i];
                if (y < 0 || y >= mc.R || x < 0 || x >= mc.C) continue;
                if (mc.bomb[y][x]) continue;
                if (visited[y][x]) continue;

                parentRow[y][x] = cur[0];
                parentCol[y][x] = cur[1];
                deque.push(new int[]{y, x, cur[2] + 1});
            }
        }
        return -1;
    }

    private List<int[]> reconstructPath(int[][] parentRow, int[][] parentCol, int destR, int destC) {
        LinkedList<int[]> path = new LinkedList<>();
        int r = destR, c = destC;
        while (r != -1) {
            path.addFirst(new int[]{r, c});
            int pr = parentRow[r][c];
            int pc = parentCol[r][c];
            r = pr;
            c = pc;
        }
        return path;
    }
}
