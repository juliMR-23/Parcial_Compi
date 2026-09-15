package algorithms;

import utils.MinefieldCase;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFSSolver {
    // TODO: Implementar por el usuario

   private final MinefieldCase mc;

    public BFSSolver(MinefieldCase mc) {
        this.mc = mc;
    }

    public int bfs()

    {
        if (mc.bomb[mc.startRow][mc.startCol] || mc.bomb[mc.finalRow][mc.finalCol]) return -1;

        int[][] dist = new int[mc.R][mc.C];
        for (int[] row : dist) Arrays.fill(row, -1);
        Queue<int[]> queue = new LinkedList<int[]>();
        dist[mc.startRow][mc.startCol] = 0;
        queue.offer(new int[]{mc.startRow, mc.startCol});

        int[] dRow = {-1,1,0,0};
        int[] dCol = {0,0,-1,1};


        while (!queue.isEmpty()){
            int[]  cur = queue.poll();
            for(int i = 0; i < 4; i++){
                int y = cur[0] + dRow[i];
                int x = cur[1] + dCol[i];
                if(y < 0 || y >= mc.R || x < 0 || x >= mc.C ) continue;
                if(mc.bomb[y][x]) continue;
                if(dist[y][x] != -1) continue;
                dist[y][x] = dist[cur[0]][cur[1]] + 1;
                queue.add(new int[]{y,x});
            }

        }
        return dist[mc.finalRow][mc.finalCol];



    }
}
