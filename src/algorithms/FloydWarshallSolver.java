package algorithms;

import models.Edge;

import java.util.*;

/**
 * Floyd-Warshall para caminos máximos entre todos los pares de nodos.
 *
 * Complejidad temporal:  O(V^3) — triple bucle sobre V nodos + O(V^3) para el pase de unbounded.
 * Complejidad espacial: O(V^2) — matrices distances y next de V×V.
 *
 * Elegido porque: la misión pide el churun máximo entre S y D, y Floyd-Warshall
 * calcula todos los pares en una sola pasada. La maximización se logra usando
 * Long.MIN_VALUE como infinito y la comparación ">" en vez de "<".
 * Después del triple bucle, se marca (i,j) como ilimitado si existe k tal que
 * d[i][k] es finito, d[k][k] > 0 y d[k][j] es finito.
 */

public class FloydWarshallSolver {
    private final int V;
    private final long[][] distances;
    private final int[][] next;
    private boolean[][] unbounded;

    public FloydWarshallSolver(int V) {
        this.V = V;
        distances = new long[V][V];
        next = new int[V][V];
        unbounded = new boolean[V][V];
        for (int i = 0; i < V; i++) {
            Arrays.fill(distances[i], Long.MIN_VALUE);
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
                if (distances[i][k] == Long.MIN_VALUE) continue;
                for (int j = 0; j < V; j++) {
                    if (distances[k][j] == Long.MIN_VALUE) continue;
                    long newDist = distances[i][k] + distances[k][j];
                    if (newDist > distances[i][j]) {
                        distances[i][j] = newDist;
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (unbounded[i][j]) continue;
                for (int k = 0; k < V; k++) {
                    if (distances[i][k] != Long.MIN_VALUE &&
                        distances[k][k] > 0 &&
                        distances[k][j] != Long.MIN_VALUE) {
                        unbounded[i][j] = true;
                        break;
                    }
                }
            }
        }

        return distances;
    }

    public long[][] getDistances() {
        return distances;
    }

    public boolean isUnbounded(int i, int j) {
        return unbounded[i][j];
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
        boolean[] inCycle = new boolean[V];
        for (int i = 0; i < V; i++) {
            if (distances[i][i] > 0) inCycle[i] = true;
        }
        for (int i = 0; i < V; i++) {
            if (!inCycle[i]) continue;
            for (int j = 0; j < V; j++) {
                if (i != j && next[i][j] != -1 && distances[i][j] > 0) {
                    cycleEdges.add(new Edge(i, j, (int) distances[i][j]));
                }
            }
        }
        return cycleEdges;
    }
}
