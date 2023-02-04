package parsers;

import java.util.Map;

public class NearbyExpr implements Expression{
    private final Direction dir;

    public NearbyExpr(String command, Direction dir) throws SyntaxError {
        if(!command.equals("nearby"))
            throw new SyntaxError("Unknown command: " + command);
        this.dir = dir;
    }

    public int evaluate(Map<String, Integer> bindings) throws SyntaxError {
        return 0;
    }
}
