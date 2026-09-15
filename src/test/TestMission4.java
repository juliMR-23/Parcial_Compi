package test;

import algorithms.KruskalSolver;
import models.Edge;
import parsers.Mission4Case;
import parsers.Mission4Parser;
import java.util.List;

public class TestMission4 {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        testSampleCase();
        testDisconnected();
        testSingleNode();
        testLinearGraph();
        System.out.println("\nMission 4: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    static void testSampleCase() throws Exception {
        String input = "1 4 5 1 2 10 2 3 20 3 4 30 4 1 40 1 3 15";
        List<Mission4Case> cases = Mission4Parser.parse(input);
        Mission4Case mc = cases.get(0);
        KruskalSolver solver = new KruskalSolver(mc.N);
        for (Edge e : mc.edges) {
            solver.addEdge(e.getSource(), e.getTarget(), e.getWeight());
        }
        assertEquals("MST cost", 55, solver.kruskal());
        assertEquals("MST edges count", 3, solver.getMstEdges().size());
    }

    static void testDisconnected() throws Exception {
        String input = "1 4 2 1 2 10 3 4 20";
        List<Mission4Case> cases = Mission4Parser.parse(input);
        Mission4Case mc = cases.get(0);
        KruskalSolver solver = new KruskalSolver(mc.N);
        for (Edge e : mc.edges) {
            solver.addEdge(e.getSource(), e.getTarget(), e.getWeight());
        }
        assertEquals("Disconnected graph", -1, solver.kruskal());
    }

    static void testSingleNode() throws Exception {
        String input = "1 1 0";
        List<Mission4Case> cases = Mission4Parser.parse(input);
        Mission4Case mc = cases.get(0);
        KruskalSolver solver = new KruskalSolver(mc.N);
        for (Edge e : mc.edges) {
            solver.addEdge(e.getSource(), e.getTarget(), e.getWeight());
        }
        assertEquals("Single node MST cost", 0, solver.kruskal());
        assertEquals("Single node MST edges", 0, solver.getMstEdges().size());
    }

    static void testLinearGraph() throws Exception {
        String input = "1 3 2 1 2 5 2 3 10";
        List<Mission4Case> cases = Mission4Parser.parse(input);
        Mission4Case mc = cases.get(0);
        KruskalSolver solver = new KruskalSolver(mc.N);
        for (Edge e : mc.edges) {
            solver.addEdge(e.getSource(), e.getTarget(), e.getWeight());
        }
        assertEquals("Linear graph MST cost", 15, solver.kruskal());
        assertEquals("Linear graph MST edges", 2, solver.getMstEdges().size());
    }

    static void assertEquals(String name, long expected, long actual) {
        if (expected == actual) {
            System.out.println("  PASS: " + name + " = " + actual);
            passed++;
        } else {
            System.out.println("  FAIL: " + name + " expected=" + expected + " actual=" + actual);
            failed++;
        }
    }
}
