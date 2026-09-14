package utils;

public class MinefieldCase {
    public final int R, C;
    public final boolean[][] bomb;
    public final int startRow, startCol;
    public final int finalRow, finalCol;

    public MinefieldCase(int R, int C, boolean[][] bomb,
                         int startRow, int startCol,
                         int finalRow, int finalCol) {
        this.R = R;
        this.C = C;
        this.bomb = bomb;
        this.startRow = startRow;
        this.startCol = startCol;
        this.finalRow = finalRow;
        this.finalCol = finalCol;
    }
}
