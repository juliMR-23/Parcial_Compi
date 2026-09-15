package models;

public class GridMap {
    private final int rows;
    private final int cols;
    private final boolean[][] bombs;
    private final int startRow, startCol;
    private final int endRow, endCol;

    public GridMap(int rows, int cols, boolean[][] bombs,
                   int startRow, int startCol, int endRow, int endCol) {
        this.rows = rows;
        this.cols = cols;
        this.bombs = bombs;
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public boolean hasBomb(int row, int col) { return bombs[row][col]; }
    public boolean[][] getBombs() { return bombs; }
    public int getStartRow() { return startRow; }
    public int getStartCol() { return startCol; }
    public int getEndRow() { return endRow; }
    public int getEndCol() { return endCol; }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean isWalkable(int row, int col) {
        return inBounds(row, col) && !bombs[row][col];
    }
}
