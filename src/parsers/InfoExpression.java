package parsers;

import java.util.Map;

public class InfoExpression implements Expression {

    private enum InfoExpressionOperation {opponent,nearby}

    private final InfoExpressionOperation command;
    private final Direction dir;

    public InfoExpression(String command, Direction dir) throws SyntaxError {
        try { this.command = InfoExpressionOperation.valueOf(command); }
        catch (IllegalArgumentException | NullPointerException e) {
            throw new SyntaxError("Unknown command: " + command);
        }
        this.dir = dir;
    }

    public int evaluate(Map<String, Integer> bindings) throws SyntaxError {
        return 0;
    }
}
