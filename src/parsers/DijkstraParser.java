package parsers;

import models.Edge;
import utils.InputParser;
import utils.InvalidInputException;
import utils.TokenReader;

import java.util.ArrayList;
import java.util.List;

public class DijkstraParser {

    @SuppressWarnings("unchecked")
    public static List<DijkstraCase> parse(String rawInput) throws InvalidInputException {
        InputParser tokenizer = new InputParser();
        String[] tokens = tokenizer.tokenize(rawInput.trim());
        TokenReader reader = new TokenReader(tokens);

        int T = reader.nextInt();
        List<DijkstraCase> cases = new ArrayList<>();

        for (int t = 0; t < T; t++) {
            int N = reader.nextInt();
            int C = reader.nextInt();
            int S = reader.nextInt();
            int D = reader.nextInt();

            List<Edge>[] adj = new ArrayList[N];
            for (int i = 0; i < N; i++) {
                adj[i] = new ArrayList<>();
            }

            for (int i = 0; i < C; i++) {
                int a = reader.nextInt();
                int b = reader.nextInt();
                int w = reader.nextInt();
                adj[a].add(new Edge(a, b, w));
                adj[b].add(new Edge(b, a, w));
            }

            cases.add(new DijkstraCase(N, adj, S, D));
        }

        return cases;
    }
}
