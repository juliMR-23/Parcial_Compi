package parsers;

import models.Edge;
import java.util.List;

public class Mission4Case {
    public final int N;
    public final int C;
    public final List<Edge> edges;

    public Mission4Case(int N, int C, List<Edge> edges) {
        this.N = N;
        this.C = C;
        this.edges = edges;
    }
}
