package algorithms;

import java.util.Arrays;

public class FloydWarshallSolver {
    private int V;
    private long[][] distances;//long, because this accumulates a lot distances

    public FloydWarshallSolver(int V) {
        this.V = V;
        distances = new long[V][V];
        for (int i = 0; i < V; i++) {
            Arrays.fill(distances[i], Long.MAX_VALUE); //distances as infinite
            distances[i][i] = 0; //distance "to myself" = 0
        }
    }

    public void addEdge(int u, int v, int w) {
        distances[u][v] = w; //the weight between the nodes is their "direct" distance
    }

    public long[][] solve() { //find "heaviest" route between every pair of nodes
        // it can take a path directly p2p or through multiple nodes and edges
        // we want to maximize, find the path with the most churun
        for (int k = 0; k < V; k++) {//node in the "middle"
            for (int i = 0; i < V; i++) {//source node
                for (int j = 0; j < V; j++) {//target node
                    if (distances[i][k] != Long.MAX_VALUE &&
                            distances[k][j] != Long.MAX_VALUE) {//don't sum infinite
                        long newDist = distances[i][k] + distances[k][j];
                        if (newDist > distances[i][j]) //save new distance if its bigger
                            distances[i][j] = newDist; //maximize churun =^._.^=
                    }
                }
            }
        }
        return distances;
    }
}
