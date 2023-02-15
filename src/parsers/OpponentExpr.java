package parsers;

import java.util.Map;

public class OpponentExpr implements Expression {

    /** Create an evaluator for opponent command
     * @param command must be "opponent"
     * @throws SyntaxError if the input command is not "opponent"
     */
    public OpponentExpr(String command) throws SyntaxError {
        if(!command.equals("opponent"))
            throw new SyntaxError("Unknown command: " + command, null);
    }
    public int evaluate(Map<String, Integer> bindings) throws SyntaxError {
        return 0;
    }
}
