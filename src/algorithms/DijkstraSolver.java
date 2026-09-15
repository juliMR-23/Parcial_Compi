package algorithms;

import models.Edge;
import parsers.DijkstraCase;

import java.util.*;

public class DijkstraSolver {

    private final DijkstraCase dc;
    private List<Edge> lastPathEdges = new ArrayList<>();

    public DijkstraSolver(DijkstraCase dc) {
        this.dc = dc;
    }

    public List<Edge> getPathEdges() {
        return lastPathEdges;
    }

    public long solve() {
        lastPathEdges = new ArrayList<>();
        if (dc.start == dc.dest) return 0;

        long[] dist = new long[dc.N];
        int[] parent = new int[dc.N];
        Arrays.fill(dist, Long.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[dc.start] = 0;

        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
        pq.add(new long[]{dc.start, 0});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            int u = (int) cur[0];
            long d = cur[1];

            if (d > dist[u]) continue;
            if (u == dc.dest) break;

            for (Edge e : dc.adj[u]) {
                long nd = d + e.getWeight();
                int v = e.getTarget();
                if (nd < dist[v]) {
                    dist[v] = nd;
                    parent[v] = u;
                    pq.add(new long[]{v, nd});
                }
            }
        }

        if (dist[dc.dest] == Long.MAX_VALUE) return -1;

        lastPathEdges = reconstructPath(parent, dc.start, dc.dest);
        return dist[dc.dest];
    }

    private List<Edge> reconstructPath(int[] parent, int from, int to) {
        List<Edge> path = new ArrayList<>();
        int cur = to;
        while (cur != from) {
            int prev = parent[cur];
            path.add(new Edge(prev, cur, 0));
            cur = prev;
        }
        Collections.reverse(path);
        return path;
    }
}
