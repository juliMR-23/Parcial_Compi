package algorithms;

import models.Edge;
import models.UnionFind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KruskalSolver {
    private int V;
    private List<Edge> edges;
    private List<Edge> lastMstEdges = new ArrayList<>();

    public KruskalSolver(int V) {
        this.V = V;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int u, int v, int weight) {
        edges.add(new Edge(u, v, weight));
        edges.add(new Edge(v, u, weight));
    }

    public List<Edge> getMstEdges() {
        return lastMstEdges;
    }

    public long kruskal() {
        lastMstEdges = new ArrayList<>();
        UnionFind uf = new UnionFind(V);
        Collections.sort(edges);
        long totalCost = 0;
        for (Edge e : edges) {
            if (uf.union(e.getSource(), e.getTarget())) {
                totalCost += e.getWeight();
                lastMstEdges.add(e);
                if (lastMstEdges.size() == V - 1) break;
            }
        }
        return lastMstEdges.size() == V - 1 ? totalCost : -1;
    }
}
