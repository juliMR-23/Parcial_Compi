package algorithms;

import java.util.ArrayDeque;
import java.util.Deque;

public class DFSSolver {
    // TODO: Implementar por el usuario

    private final int R , C;
    private final int startRow, startCol;
    private final int finalRow, finalCol;
    private boolean[][] bomb;

    public DFSSolver(int r, int c, int startRow, int startCol, int finalRow, int finalCol, boolean[][] bomb) {
        R = r;
        C = c;
        this.startRow = startRow;
        this.startCol = startCol;
        this.finalRow = finalRow;
        this.finalCol = finalCol;
        this.bomb = bomb;
    }

    private int dfs(){
        if(bomb[startRow][startCol]||bomb[finalRow][finalCol]) return -1;

        boolean[][] visited = new boolean[R][C];
        visited[startRow][startCol] = true;
        Deque<int[]> deque = new ArrayDeque<>();
        deque.push(new int[]{startRow, startCol,0});
        int[] dRow = {-1,1,0,0};
        int[] dCol = {0,0,-1,1};
        while(!deque.isEmpty()){
            int[] cur = deque.pop();
            if(cur[0]== finalRow && cur[1]==finalCol) return cur[2] ;
            for(int i = 3; i >= 0; i--){
                int y =  cur[0] + dRow[i];
                int x = cur[1] + dCol[i];
                if(y < 0 || y >= R || x < 0 || x >= C) continue;
                if(visited[y][x]) continue;
                if(bomb[y][x]) continue;
                visited[y][x] = true;
                deque.push(new int[]{y,x,cur[2]+1});
            }
        }
        return -1 ;
    }
}
