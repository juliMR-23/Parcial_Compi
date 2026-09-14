package algorithms;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFSSolver {
    // TODO: Implementar por el usuario

    private int R;
    private int C;
    private final int startRow,  startCol;
    private final int finalRow,  finalCol;
    private final boolean[][] bomb;

    public BFSSolver(int R, int C,int startRow, int startCol, boolean[][] bomb, int finalRow, int finalCol) {
        this.R = R;
        this.C = C;
        this.startRow = startRow;
        this.startCol = startCol;
        this.finalRow = finalRow;
        this.finalCol = finalCol;
        this.bomb = bomb;
    }

    public int bfs()

    {
        if (bomb[startRow][startCol] || bomb[finalRow][finalCol]) return -1;

        int[][] dist = new int[R][C];
        for (int[] row : dist) Arrays.fill(row, -1);
        Queue<int[]> queue = new LinkedList<int[]>();
        dist[startRow][startCol] = 0;
        queue.offer(new int[]{startRow, startCol});

        int[] dRow = {-1,1,0,0};
        int[] dCol = {0,0,-1,1};


        while (!queue.isEmpty()){
            int[]  cur = queue.poll();
            for(int i = 0; i < 4; i++){
                int y = cur[0] + dRow[i];
                int x = cur[1] + dCol[i];
                if(y < 0 || y >= R || x < 0 || x >= C ) continue;
                if(bomb[y][x]) continue;
                if(dist[y][x] != -1) continue;
                dist[y][x] = dist[cur[0]][cur[1]] + 1;
                queue.add(new int[]{y,x});
            }

        }
        return dist[finalRow][finalCol];



    }
}
