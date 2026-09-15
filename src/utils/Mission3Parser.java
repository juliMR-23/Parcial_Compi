package utils;

import models.Edge;
import java.util.ArrayList;
import java.util.List;

public class Mission3Parser {

    public static List<Mission3Case> parse(String rawInput) throws InvalidInputException {
        InputParser tokenizer = new InputParser();
        String[] tokens = tokenizer.tokenize(rawInput);
        TokenReader reader = new TokenReader(tokens);

        int T = reader.nextInt();
        List<Mission3Case> cases = new ArrayList<>();

        for (int t = 0; t < T; t++) {
            int N = reader.nextInt();
            int M = reader.nextInt();
            int S = reader.nextInt();
            int D = reader.nextInt();

            List<Edge> edges = new ArrayList<>();
            for (int i = 0; i < M; i++) {
                int a = reader.nextInt();
                int b = reader.nextInt();
                int w = reader.nextInt();
                edges.add(new Edge(a, b, w));
            }

            cases.add(new Mission3Case(N, M, S, D, edges));
        }

        return cases;
    }
}
