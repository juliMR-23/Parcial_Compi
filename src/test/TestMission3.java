package test;

import algorithms.BellmanFordSolver;
import algorithms.FloydWarshallSolver;
import models.Edge;
import parsers.Mission3Case;
import parsers.Mission3Parser;
import java.util.List;

public class TestMission3 {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        testSampleCase1();
        testPositiveCycle();
        testUnreachable();
        testNoCycleDetected();
        System.out.println("\nMission 3: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    static void testSampleCase1() throws Exception {
        String input = "1 5 7 0 4 0 1 50 0 2 10 1 2 -30 1 3 40 2 1 -5 2 3 60 3 4 20";
        List<Mission3Case> cases = Mission3Parser.parse(input);
        Mission3Case mc = cases.get(0);
        FloydWarshallSolver floyd = new FloydWarshallSolver(mc.N);
        BellmanFordSolver bellman = new BellmanFordSolver(mc.N);
        for (Edge e : mc.edges) {
            floyd.addEdge(e.getSource(), e.getTarget(), e.getWeight());
            bellman.addEdge(e.getSource(), e.getTarget(), e.getWeight());
        }
        long[][] fwr = floyd.solve();
        long[] bfr = bellman.solve(mc.S);
        assertEquals("Max churun S->D", 110, fwr[mc.S][mc.D]);
        assertEquals("Bellman dist S->D", 110, bfr[mc.D]);
        assertEquals("Not affected by cycle", false, bellman.canReachFromCycle(mc.D));
    }

    static void testPositiveCycle() throws Exception {
        String input = "1 4 4 0 3 0 1 20 1 2 30 2 1 -10 2 3 15";
        List<Mission3Case> cases = Mission3Parser.parse(input);
        Mission3Case mc = cases.get(0);
        FloydWarshallSolver floyd = new FloydWarshallSolver(mc.N);
        BellmanFordSolver bellman = new BellmanFordSolver(mc.N);
        for (Edge e : mc.edges) {
            floyd.addEdge(e.getSource(), e.getTarget(), e.getWeight());
            bellman.addEdge(e.getSource(), e.getTarget(), e.getWeight());
        }
        floyd.solve();
        bellman.solve(mc.S);
        assertEquals("Affected by positive cycle", true, bellman.canReachFromCycle(mc.D));
    }

    static void testUnreachable() throws Exception {
        String input = "1 3 3 0 2 0 1 -40 1 2 -25 0 2 -80";
        List<Mission3Case> cases = Mission3Parser.parse(input);
        Mission3Case mc = cases.get(0);
        BellmanFordSolver bellman = new BellmanFordSolver(mc.N);
        for (Edge e : mc.edges) {
            bellman.addEdge(e.getSource(), e.getTarget(), e.getWeight());
        }
        long[] bfr = bellman.solve(mc.S);
        assertEquals("Bellman dist to unreachable", -65, bfr[mc.D]);
    }

    static void testNoCycleDetected() throws Exception {
        String input = "1 3 2 0 1 0 1 10 1 2 20";
        List<Mission3Case> cases = Mission3Parser.parse(input);
        Mission3Case mc = cases.get(0);
        BellmanFordSolver bellman = new BellmanFordSolver(mc.N);
        for (Edge e : mc.edges) {
            bellman.addEdge(e.getSource(), e.getTarget(), e.getWeight());
        }
        bellman.solve(mc.S);
        assertEquals("No cycle in simple path", false, bellman.canReachFromCycle(1));
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

    static void assertEquals(String name, boolean expected, boolean actual) {
        if (expected == actual) {
            System.out.println("  PASS: " + name + " = " + actual);
            passed++;
        } else {
            System.out.println("  FAIL: " + name + " expected=" + expected + " actual=" + actual);
            failed++;
        }
    }
}
