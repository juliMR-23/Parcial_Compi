package test;

import algorithms.DijkstraSolver;
import parsers.DijkstraCase;
import parsers.DijkstraParser;
import java.util.List;

public class TestMission2 {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        testSampleCase1();
        testSampleCase2();
        testUnreachable();
        testStartEqualsDest();
        System.out.println("\nMission 2: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    static void testSampleCase1() throws Exception {
        String input = "1 2 1 0 1 0 1 100";
        List<DijkstraCase> cases = DijkstraParser.parse(input);
        DijkstraSolver solver = new DijkstraSolver(cases.get(0));
        assertEquals("Case 1 direct edge", 100, solver.solve());
        assertEquals("Case 1 path edges", 1, solver.getPathEdges().size());
    }

    static void testSampleCase2() throws Exception {
        String input = "1 3 3 2 0 0 1 100 0 2 200 1 2 50";
        List<DijkstraCase> cases = DijkstraParser.parse(input);
        DijkstraSolver solver = new DijkstraSolver(cases.get(0));
        assertEquals("Case 2 indirect path", 150, solver.solve());
        assertEquals("Case 2 path edges", 2, solver.getPathEdges().size());
    }

    static void testUnreachable() throws Exception {
        String input = "1 2 0 0 1";
        List<DijkstraCase> cases = DijkstraParser.parse(input);
        DijkstraSolver solver = new DijkstraSolver(cases.get(0));
        assertEquals("Unreachable dest", -1, solver.solve());
        assertEquals("No path edges", 0, solver.getPathEdges().size());
    }

    static void testStartEqualsDest() throws Exception {
        String input = "1 3 2 1 1 0 1 100 1 2 50";
        List<DijkstraCase> cases = DijkstraParser.parse(input);
        DijkstraSolver solver = new DijkstraSolver(cases.get(0));
        assertEquals("Start==Dest", 0, solver.solve());
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
