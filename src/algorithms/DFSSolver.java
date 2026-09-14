package algorithms;

import utils.MinefieldCase;

import java.util.ArrayDeque;
import java.util.Deque;

public class DFSSolver {
    // TODO: Implementar por el usuario

   private final MinefieldCase mc;

    public DFSSolver(MinefieldCase mc) {
        this.mc = mc;
    }

    private int dfs(){
        if(mc.bomb[mc.startRow][mc.startCol]||mc.bomb[mc.finalRow][mc.finalCol]) return -1;

        boolean[][] visited = new boolean[mc.R][mc.C];
        visited[mc.startRow][mc.startCol] = true;
        Deque<int[]> deque = new ArrayDeque<>();
        deque.push(new int[]{mc.startRow, mc.startCol,0});
        int[] dRow = {-1,1,0,0};
        int[] dCol = {0,0,-1,1};
        while(!deque.isEmpty()){
            int[] cur = deque.pop();
            if(cur[0]== mc.finalRow && cur[1]==mc.finalCol) return cur[2] ;
            for(int i = 3; i >= 0; i--){
                int y =  cur[0] + dRow[i];
                int x = cur[1] + dCol[i];
                if(y < 0 || y >= mc.R || x < 0 || x >= mc.C) continue;
                if(visited[y][x]) continue;
                if(mc.bomb[y][x]) continue;
                visited[y][x] = true;
                deque.push(new int[]{y,x,cur[2]+1});
            }
        }
        return -1 ;
    }
}
