package algorithms;

import models.Edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BellmanFordSolver {
    private int V;

    private List<Edge> edge;

    public BellmanFordSolver(int V) {
        this.V = V;
        edge = new ArrayList<Edge>();
    }

    public void addEdge(int u, int v, int w) {
        edge.add(new Edge(u, v, w));
    }

    public long[] solve(int start) {
        long[] distances = new long[V];
        Arrays.fill(distances, Long.MAX_VALUE); //infinites
        distances[start] = 0; //start 0

        for (int i = 0; i <= V; i++) {
            for (Edge e : edge) {
                if (distances[e.getSource()] != Long.MAX_VALUE){//don't sum infinite
                    long newDist=distances[e.getSource()] + e.getWeight();
                    if(newDist > distances[e.getTarget()]) {//if the new one is bigger, save it
                        distances[e.getTarget()] = newDist; //more food for the cats
                        if(i==V){// already connected everything (V-1) and can still
                            /// TODO: label as infinite churun, show cycle in the UI
                        }
                    }
                }
            }
        }
        return distances;
    }
}
