package algorithms;

import models.Edge;

import java.util.*;

public class FloydWarshallSolver {
    private int V;
    private long[][] distances;
    private int[][] next;
    private boolean[] inPositiveCycle;

    public FloydWarshallSolver(int V) {
        this.V = V;
        distances = new long[V][V];
        next = new int[V][V];
        inPositiveCycle = new boolean[V];
        for (int i = 0; i < V; i++) {
            Arrays.fill(distances[i], Long.MAX_VALUE);
            Arrays.fill(next[i], -1);
            distances[i][i] = 0;
        }
    }

    public void addEdge(int u, int v, int w) {
        distances[u][v] = w;
        next[u][v] = v;
    }

    public long[][] solve() {
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (distances[i][k] != Long.MAX_VALUE &&
                            distances[k][j] != Long.MAX_VALUE) {
                        long newDist = distances[i][k] + distances[k][j];
                        if (newDist > distances[i][j]) {
                            distances[i][j] = newDist;
                            next[i][j] = next[i][k];
                        }
                    }
                }
            }
        }

        for (int i = 0; i < V; i++) {
            if (distances[i][i] > 0) {
                inPositiveCycle[i] = true;
            }
        }
        return distances;
    }

    public boolean isInPositiveCycle(int node) {
        return inPositiveCycle[node];
    }

    public List<Edge> getPathEdges(int from, int to) {
        List<Edge> path = new ArrayList<>();
        if (next[from][to] == -1) return path;
        int cur = from;
        while (cur != to) {
            int nxt = next[cur][to];
            if (nxt == -1) break;
            path.add(new Edge(cur, nxt, (int) distances[cur][nxt]));
            cur = nxt;
        }
        return path;
    }

    public List<Edge> getCycleEdges() {
        List<Edge> cycleEdges = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            if (inPositiveCycle[i]) {
                for (int j = 0; j < V; j++) {
                    if (i != j && next[i][j] != -1 && distances[i][j] > 0) {
                        cycleEdges.add(new Edge(i, j, (int) distances[i][j]));
                    }
                }
            }
        }
        return cycleEdges;
    }
}
