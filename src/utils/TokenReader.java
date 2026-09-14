 package utils;

    public class TokenReader {
        private final String[] tokens;
        private int pos = 0;

        public TokenReader(String[] tokens) {
            this.tokens = tokens;
        }

        public int nextInt() {
            return Integer.parseInt(tokens[pos++]);
        }

        public boolean hasNext() {
            return pos < tokens.length;
        }
    }

