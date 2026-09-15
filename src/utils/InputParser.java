package utils;

public class InputParser {
    public String[] tokenize(String input) throws InvalidInputException {
        if (input == null || input.isBlank()) {
            throw new InvalidInputException("La entrada está vacía.");
        }
        String[] tokens = input.trim().split("\\s+");
        if (tokens.length == 0) {
            throw new InvalidInputException("La entrada no contiene tokens válidos.");
        }
        return tokens;
    }
}
