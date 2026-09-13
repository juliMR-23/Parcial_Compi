package models;

public class UnionFind{
    private int[] parent;
    private int[] rank;
    public UnionFind (int n){
        parent = new int[n];
        for (int i = 0; i < n; i++)
            parent[i]=i;
    }
    public int find(int x){//returns the root
        if(parent[x]!=x)
            parent[x]=find(parent[x]);
        return parent[x];
    }
    public boolean union(int a, int b){
        int rx=find(a);//root a
        int ry=find(b);//root b

        //same root, they are already connected
        if(rx==ry) return false;//false, no union created

        /// When they are not connected we use the rank
        //make parent the one with higher rank
        if(rank[rx] < rank[ry])
            parent[rx] = ry;
        else if(rank[rx] > rank[ry])
            parent[ry] = rx;
        else { /// same rank
            parent[ry]=rx;//whatever
            rank[rx]++;//became parent -> +1 rank
        }
        return true;//new union :)
    }
}
