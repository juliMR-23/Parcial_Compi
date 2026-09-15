package parsers;

import utils.InputParser;
import utils.InvalidInputException;
import utils.TokenReader;

import java.util.ArrayList;
import java.util.List;

public class MineFieldParser {

    public static List<MinefieldCase> parse(String rawInput) throws InvalidInputException {
        InputParser tokenizer = new InputParser();
        String[] tokens = tokenizer.tokenize(rawInput);
        TokenReader reader = new TokenReader(tokens);

        List<MinefieldCase> cases = new ArrayList<>();

        while (reader.hasNext()) {
            int R = reader.nextInt();
            int C = reader.nextInt();
            if (R == 0 && C == 0) break;

            if (R < 1 || C < 1) {
                throw new InvalidInputException("Dimensiones invalidas: " + R + "x" + C);
            }

            boolean[][] bomb = new boolean[R][C];
            int bombRows = reader.nextInt();
            for (int i = 0; i < bombRows; i++) {
                int row = reader.nextInt();
                int count = reader.nextInt();
                for (int j = 0; j < count; j++) {
                    int col = reader.nextInt();
                    bomb[row][col] = true;
                }
            }

            int startRow = reader.nextInt();
            int startCol = reader.nextInt();
            int destRow = reader.nextInt();
            int destCol = reader.nextInt();

            cases.add(new MinefieldCase(R, C, bomb, startRow, startCol, destRow, destCol));
        }

        return cases;
    }
}
