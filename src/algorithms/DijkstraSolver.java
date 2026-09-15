package algorithms;

import models.Edge;
import parsers.DijkstraCase;
import java.util.Arrays;
import java.util.PriorityQueue;

public class DijkstraSolver {

    private final DijkstraCase dc;

    public DijkstraSolver(DijkstraCase dc) {
        this.dc = dc;
    }


    public long solve() {
        if (dc.start == dc.dest) return 0;

        long[] dist = new long[dc.N];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[dc.start] = 0;

        // cada entrada: {nodo, distancia acumulada}
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        pq.add(new long[]{dc.start, 0});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            int u = (int) cur[0];
            long d = cur[1];

            if (d > dist[u]) continue;

            if (u == dc.dest) return d; 

            for (Edge e : dc.adj[u]) {
                long nd = d + e.getWeight();
                int v = e.getTarget();
                if (nd < dist[v]) {
                    dist[v] = nd;
                    pq.add(new long[]{v, nd});
                }
            }
        }

        return dist[dc.dest] == Long.MAX_VALUE ? -1 : dist[dc.dest];
    }
}


