package algorithms;

import models.Edge;
import models.UnionFind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Kruskal para Árbol de Expansión Mínima (MST).
 *
 * Complejidad temporal:  O(E log E) — ordenar aristas + O(E × α(V)) de Union-Find (amortizado casi constante).
 * Complejidad espacial: O(V + E) — UnionFind de tamaño V+1 + lista de aristas.
 *
 * Elegido porque: Kruskal es más simple de implementar que Prim, funciona bien
 * con listas de aristas dispersas, y el problema pide minimizar el costo total
 * de conexiones (cableado de la guarida). Los nodos son 1-indexados,
 * por eso UnionFind usa tamaño V+1.
 */

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
        UnionFind uf = new UnionFind(V + 1);
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
