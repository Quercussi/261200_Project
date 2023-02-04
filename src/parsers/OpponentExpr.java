package parsers;

import java.util.Map;

public class OpponentExpr implements Expression {

    public OpponentExpr(String command) throws SyntaxError {
        if(!command.equals("opponent"))
            throw new SyntaxError("Unknown command: " + command);
    }
    public int evaluate(Map<String, Integer> bindings) throws SyntaxError {
        return 0;
    }
}
