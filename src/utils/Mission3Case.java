package utils;

import models.Edge;
import java.util.List;

public class Mission3Case {
    public final int N;
    public final int M;
    public final int S;
    public final int D;
    public final List<Edge> edges;

    public Mission3Case(int N, int M, int S, int D, List<Edge> edges) {
        this.N = N;
        this.M = M;
        this.S = S;
        this.D = D;
        this.edges = edges;
    }
}
