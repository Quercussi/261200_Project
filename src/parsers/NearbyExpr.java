package parsers;

import java.util.Map;

public class NearbyExpr implements Expression{
    private final Direction dir;

    /** Create an evaluator for nearby command
     * @param command must be "nearby"
     * @param dir is not null
     * @throws SyntaxError if the input command is not "nearby"
     */
    public NearbyExpr(String command, Direction dir) throws SyntaxError {
        if(!command.equals("nearby"))
            throw new SyntaxError("Unknown command: " + command, null);
        this.dir = dir;
    }

    public int evaluate(Map<String, Integer> bindings) throws SyntaxError {
        return 0;
    }
}
