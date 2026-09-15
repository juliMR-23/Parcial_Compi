package utils;

import models.Edge;
import java.util.List;

public class DijkstraCase {
    public final int N;
    public final List<Edge>[] adj;
    public final int start;
    public final int dest;

    public DijkstraCase(int N, List<Edge>[] adj, int start, int dest) {
        this.N = N;
        this.adj = adj;
        this.start = start;
        this.dest = dest;
    }
}