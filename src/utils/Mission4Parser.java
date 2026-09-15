package utils;

import models.Edge;
import java.util.ArrayList;
import java.util.List;

public class Mission4Parser {

    public static List<Mission4Case> parse(String rawInput) throws InvalidInputException {
        InputParser tokenizer = new InputParser();
        String[] tokens = tokenizer.tokenize(rawInput);
        TokenReader reader = new TokenReader(tokens);

        int T = reader.nextInt();
        List<Mission4Case> cases = new ArrayList<>();

        for (int t = 0; t < T; t++) {
            int N = reader.nextInt();
            int C = reader.nextInt();

            List<Edge> edges = new ArrayList<>();
            for (int i = 0; i < C; i++) {
                int u = reader.nextInt();
                int v = reader.nextInt();
                int w = reader.nextInt();
                edges.add(new Edge(u, v, w));
            }

            cases.add(new Mission4Case(N, C, edges));
        }

        return cases;
    }
}
