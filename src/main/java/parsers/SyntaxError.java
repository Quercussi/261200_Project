package parsers;

public class SyntaxError extends Exception {
    private final Integer line;

    public SyntaxError(String msg, Integer line){
        super(msg + (line == null ? "" : " at line " + line));
        this.line = line;
    }

    public Integer getLine() { return line; }
}