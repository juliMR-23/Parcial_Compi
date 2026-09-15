package models;

public class Node {
    private final int id;
    private double x;
    private double y;
    private String label;

    public Node(int id) {
        this.id = id;
        this.x = 0;
        this.y = 0;
        this.label = String.valueOf(id);
    }

    public Node(int id, String label) {
        this.id = id;
        this.x = 0;
        this.y = 0;
        this.label = label;
    }

    public int getId() { return id; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        return id == ((Node) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
