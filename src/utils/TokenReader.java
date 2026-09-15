package utils;

public class TokenReader {
    private final String[] tokens;
    private int pos = 0;

    public TokenReader(String[] tokens) {
        this.tokens = tokens;
    }

    public int nextInt() throws InvalidInputException {
        if (pos >= tokens.length) {
            throw new InvalidInputException("Se esperaba un entero pero no hay más tokens (posición " + pos + ").");
        }
        String token = tokens[pos];
        try {
            int value = Integer.parseInt(token);
            pos++;
            return value;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Token inválido: '" + token + "' no es un entero.", e);
        }
    }

    public boolean hasNext() {
        return pos < tokens.length;
    }
}
