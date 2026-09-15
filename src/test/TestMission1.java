package test;

import algorithms.BFSSolver;
import algorithms.DFSSolver;
import parsers.MineFieldParser;
import parsers.MinefieldCase;
import java.util.List;

public class TestMission1 {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        testSampleCase();
        testUnreachable();
        testStartEqualsEnd();
        testSingleCell();
        System.out.println("\nMission 1: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    static void testSampleCase() throws Exception {
        String input = "10 10 9 0 1 2 1 1 2 2 2 2 9 3 2 1 7 5 3 3 6 9 6 4 0 1 2 7 7 3 0 3 8 8 2 7 9 9 3 2 3 4 0 0 9 9 0 0";
        List<MinefieldCase> cases = MineFieldParser.parse(input);
        MinefieldCase mc = cases.get(0);
        BFSSolver bfs = new BFSSolver(mc);
        DFSSolver dfs = new DFSSolver(mc);
        assertEquals("BFS sample distance", 18, bfs.bfs());
        assertEquals("DFS sample distance", 32, dfs.dfs());
        assertEquals("BFS path size", 19, bfs.getPath().size());
        assertEquals("DFS traversal size", 33, dfs.getTraversalOrder().size());
    }

    static void testUnreachable() throws Exception {
        String input = "3 3 1 1 3 0 1 2 0 0 2 2 0 0";
        List<MinefieldCase> cases = MineFieldParser.parse(input);
        MinefieldCase mc = cases.get(0);
        BFSSolver bfs = new BFSSolver(mc);
        DFSSolver dfs = new DFSSolver(mc);
        assertEquals("BFS unreachable (wall blocks path)", -1, bfs.bfs());
        assertEquals("DFS unreachable (wall blocks path)", -1, dfs.dfs());
    }

    static void testStartEqualsEnd() throws Exception {
        String input = "3 3 1 0 1 1 0 0 0 0 0 0";
        List<MinefieldCase> cases = MineFieldParser.parse(input);
        MinefieldCase mc = cases.get(0);
        BFSSolver bfs = new BFSSolver(mc);
        DFSSolver dfs = new DFSSolver(mc);
        assertEquals("BFS start==end", 0, bfs.bfs());
        assertEquals("DFS start==end", 0, dfs.dfs());
    }

    static void testSingleCell() throws Exception {
        String input = "1 1 0 0 0 0 0";
        List<MinefieldCase> cases = MineFieldParser.parse(input);
        MinefieldCase mc = cases.get(0);
        BFSSolver bfs = new BFSSolver(mc);
        DFSSolver dfs = new DFSSolver(mc);
        assertEquals("BFS 1x1 grid", 0, bfs.bfs());
        assertEquals("DFS 1x1 grid", 0, dfs.dfs());
    }

    static void assertEquals(String name, int expected, int actual) {
        if (expected == actual) {
            System.out.println("  PASS: " + name + " = " + actual);
            passed++;
        } else {
            System.out.println("  FAIL: " + name + " expected=" + expected + " actual=" + actual);
            failed++;
        }
    }
}
