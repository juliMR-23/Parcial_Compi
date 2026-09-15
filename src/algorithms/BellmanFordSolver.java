package algorithms;

import models.Edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BellmanFordSolver {
    private int V;
    private List<Edge> edge;
    private boolean[] affectedByPositiveCycle;

    public BellmanFordSolver(int V) {
        this.V = V;
        edge = new ArrayList<>();
        affectedByPositiveCycle = new boolean[V];
    }

    public void addEdge(int u, int v, int w) {
        edge.add(new Edge(u, v, w));
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

        if (affectedByPositiveCycle[start]) {
            Arrays.fill(affectedByPositiveCycle, true);
        }
        return distances;
    }
}
