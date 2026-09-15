package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graph {
    private final List<Node> nodes;
    private final List<Edge> edges;
    private final boolean directed;

    public Graph(boolean directed) {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.directed = directed;
    }

    public Graph() {
        this(false);
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addNode(int id) {
        nodes.add(new Node(id));
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void addEdge(int source, int target, int weight) {
        edges.add(new Edge(source, target, weight));
    }

    public Node getNode(int id) {
        for (Node n : nodes) {
            if (n.getId() == id) return n;
        }
        return null;
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public List<Edge> getEdgesFrom(int nodeId) {
        List<Edge> result = new ArrayList<>();
        for (Edge e : edges) {
            if (e.getSource() == nodeId) result.add(e);
        }
        return result;
    }

    public boolean isDirected() { return directed; }
    public int getNodeCount() { return nodes.size(); }
    public int getEdgeCount() { return edges.size(); }
}
