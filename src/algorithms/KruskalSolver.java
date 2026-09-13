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
        Edge e = new Edge(u,v,weight);
        edges.add(e);
    }
    public int kruskal(){
        UnionFind uf = new UnionFind(V);
        Collections.sort(edges);
        List<Edge> mst = new ArrayList<>();
        int totalCost=0;
        for (Edge e:edges){
            if(uf.union(e.getSource(),e.getTarget())){
                totalCost += e.getWeight();
                mst.add(e);
                if (mst.size()==V-1) break;
            } //else -> Found a cycle
        }
        return mst.size() == V-1? totalCost : -1;//not all connected, return -1
        //NOTE: we need at least V-1 edges to connect V nodes
    }
}
