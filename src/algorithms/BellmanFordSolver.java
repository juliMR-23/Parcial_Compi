package algorithms;

import models.Edge;

import java.util.*;

/**
 * Bellman-Ford para detectar nodos afectados por ciclos de ganancia positiva.
 *
 * Complejidad temporal:  O(V × E) — V iteraciones sobre la lista de aristas + BFS O(V+E).
 * Complejidad espacial: O(V + E) — array distances + lista de aristas + grafo inverso para BFS.
 *
 * Elegido porque: complementa a Floyd-Warshall con la capacidad de detectar
 * propagación de ciclos desde el origen. Corre V+1 iteraciones: los nodos que
 * mejoran en la ronda extra pertenecen o son alimentados por un ciclo positivo.
 * Se propaga el marcado a todos los nodos alcanzables desde esos nodos.
 */

public class BellmanFordSolver {
    private final int V;
    private final List<Edge> edge;
    private final boolean[] affectedByPositiveCycle;
    private final List<List<Integer>> reverseAdj;

    public BellmanFordSolver(int V) {
        this.V = V;
        edge = new ArrayList<>();
        affectedByPositiveCycle = new boolean[V];
        reverseAdj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) reverseAdj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, int w) {
        edge.add(new Edge(u, v, w));
        reverseAdj.get(v).add(u);
    }

    public boolean isAffectedByPositiveCycle(int node) {
        return affectedByPositiveCycle[node];
    }

    public long[] solve(int start) {
        Arrays.fill(affectedByPositiveCycle, false);
        long[] distances = new long[V];
        Arrays.fill(distances, Long.MIN_VALUE);
        distances[start] = 0;

        for (int i = 0; i <= V; i++) {
            for (Edge e : edge) {
                if (distances[e.getSource()] != Long.MIN_VALUE) {
                    long newDist = distances[e.getSource()] + e.getWeight();
                    if (newDist > distances[e.getTarget()]) {
                        distances[e.getTarget()] = newDist;
                        if (i == V) {
                            affectedByPositiveCycle[e.getSource()] = true;
                            affectedByPositiveCycle[e.getTarget()] = true;
                        }
                    }
                }
            }
        }

        Deque<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[V];
        for (int i = 0; i < V; i++) {
            if (affectedByPositiveCycle[i]) {
                queue.add(i);
                visited[i] = true;
            }
        }
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : reverseAdj.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    affectedByPositiveCycle[v] = true;
                    queue.add(v);
                }
            }
        }

        return distances;
    }
}
