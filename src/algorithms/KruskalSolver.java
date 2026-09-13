package algorithms;

import models.Edge;
import models.UnionFind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KruskalSolver {
    private int V; //total nodes
    private List<Edge> edges; // list of edges (aristas)

    public KruskalSolver(int V) {
        this.V = V;
        this.edges = new ArrayList<>();
    }
    public void addEdge (int u, int v, int weight){
        edges.add(new Edge(u,v,weight));
        edges.add(new Edge(v,u,weight));
    }
    public long kruskal(){
        UnionFind uf = new UnionFind(V);
        Collections.sort(edges);
        List<Edge> mst = new ArrayList<>();
        long totalCost=0; //accumulator, we use long
        for (Edge e:edges){
            if(uf.union(e.getSource(),e.getTarget())){
                totalCost += e.getWeight();
                mst.add(e);
                if (mst.size()==V-1) break;
            } //else -> Found a cycle
        }
        return mst.size() == V-1? totalCost : -1;
        //all connected -> return cost, else return -1
        /// todo: Use the -1 to trigger the message "Limon cut too many cables" on the UI
        //NOTE: we need at least V-1 edges to connect V nodes
    }
}
