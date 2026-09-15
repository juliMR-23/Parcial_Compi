package models;

public class Edge implements Comparable<Edge> {
    private int source, target, weight;
    public Edge(int source, int target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        //weight is int for simplicity
        //long just for accumulations (in the algorithms)
    }

    //getters
    public int getSource() {
        return source;
    }
    public int getTarget() {
        return target;
    }
    public int getWeight() {
        return weight;
    }

    @Override //comparable edges, will be used for kruskal
    public int compareTo(Edge o) {
        return Integer.compare(weight, o.weight); // compare weights
    }
}
