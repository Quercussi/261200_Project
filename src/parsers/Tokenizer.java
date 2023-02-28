package parsers;

class Tokenizer {
    private final String src;
    private String nextToken = "";
    private int pos = 0;
    private int line = 1;

    /** Create a Tokenizer from input string
     * @param src input string to be tokenized
     * */
    public Tokenizer(String src) throws SyntaxError {
        this.src = src;
        getNextToken();
    }

    /** Get the next token of the string
     * effect: change nextToken into the next token
     * */
    private void getNextToken() throws SyntaxError {
        StringBuilder s = new StringBuilder();

        eatVacuumSyntax();

        if(pos == src.length()) {
            nextToken = null;
            return;
        }

        char nextChar = src.charAt(pos);

        // Check for numbers
        if(Character.isDigit(nextChar)) {
            do { s.append(nextChar); }
            while( (++pos < src.length()) && Character.isDigit(nextChar = src.charAt(pos)) );
        }

        // Check for identifiers
        else if(Character.isLetter(nextChar)) {
            do { s.append(nextChar); }
            while( (++pos < src.length()) && Identifier.isIdentifierChar(nextChar = src.charAt(pos)) );
        }

        // Check for operator or parenthesis
        else if(isOperator(nextChar)) {
            s.append(nextChar);
            pos++;
        }

        // LexicalError
        else throw new SyntaxError("Unknown character: " + nextChar, line);

        nextToken = s.toString();
    }

    private void eatVacuumSyntax() {
        for(; pos < src.length(); pos++){
            char ptrChar = src.charAt(pos);
            if(ptrChar == '#')
                eatComment();
            else if(ptrChar == '\n')
                line++;
            else if (!Character.isWhitespace(ptrChar))
                return;

            // This is to prevent infinite loop.
            // This can occur when the construction plan
            //  is end with comment.
            if(pos == -1) {
                pos = src.length();
                return;
            }
        }
    }

    private void eatComment() {
        pos = src.indexOf('\n',pos);
        line++;
    }

    /** Check if there is next token
     * @return whether there is next token
     * */
    public boolean hasNextToken() { return nextToken != null; }

    /** Gives the next token
     * @throws SyntaxError if there is no next token
     * @return the next token
     * */
    public String peek() throws SyntaxError {
        if(!hasNextToken()) throw new SyntaxError("No more tokens", line);
        return nextToken;
    }

    /** Gives whether the next token is the input string
     * @param s input string to be checked
     * @return whether the next token is the input string
     * */
    public boolean peek(String s) throws SyntaxError {
        if (!hasNextToken()) return false;
        return peek().equals(s);
    }

    /** Return current token and then get the next token of the string
     * effect: change nextToken into the next token
     * @throws SyntaxError if there is no next token
     * @return current token
     * */
    public String consume() throws SyntaxError {
        if(!hasNextToken()) throw new SyntaxError("No more tokens", line);
        String result = nextToken;
        getNextToken();

        return result;
    }

    /** Return current token and then get the next token of the string
     * effect: change nextToken into the next token
     * @param  s input string to be checked
     * @throws SyntaxError if the current token is not s
     * */
    public void consume(String s) throws SyntaxError{
        if(peek(s)){ consume(); }
        else throw new SyntaxError("Malformed expression: " + s, line);
    }

    /** Return the current line tokenizer is reading.
     * @return current line
     * */
    public int getLine() { return line; }

    /** Check whether the input character is an operator
     * @param c is a character which to be checked
     * @return whether the input character is an operator
     * */
    static public boolean isOperator(char c) {
        return (c == '+' || c == '-' || c == '*' || c == '/'
                || c == '%' || c == '(' || c == ')' || c == '{'
                || c == '}' || c == '^' || c == '=');
    }
}