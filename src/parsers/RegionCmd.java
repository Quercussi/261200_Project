package parsers;

import java.util.Map;

public class RegionCmd implements Statement{
    private enum RegionCmdOperation {invest,collect}

    private final Expression expr;
    private final RegionCmdOperation command;

    public RegionCmd(String command, Expression expr) throws SyntaxError {
        try { this.command = RegionCmdOperation.valueOf(command); }
        catch (IllegalArgumentException | NullPointerException e) {
            throw new SyntaxError("Unknown command: " + command);
        }
        this.expr = expr;
    }

    public void execute(Map<String, Integer> bindings) throws SyntaxError {

    }
}
